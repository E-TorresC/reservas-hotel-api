package com.hotel.reservas.service.impl;

import com.hotel.reservas.dto.request.ReservaFilter;
import com.hotel.reservas.dto.request.ReservaRequest;
import com.hotel.reservas.dto.response.ReservaResponse;
import com.hotel.reservas.entity.*;
import com.hotel.reservas.exception.BusinessRuleException;
import com.hotel.reservas.exception.ResourceNotFoundException;
import com.hotel.reservas.repository.ClienteRepository;
import com.hotel.reservas.repository.HabitacionRepository;
import com.hotel.reservas.repository.ReservaRepository;
import com.hotel.reservas.repository.specification.ReservaSpecification;
import com.hotel.reservas.service.ReservaService;
import com.hotel.reservas.mapper.ReservaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaServiceImpl implements ReservaService {

    private final ReservaRepository reservaRepository;
    private final ClienteRepository clienteRepository;
    private final HabitacionRepository habitacionRepository;

    @Override
    @Transactional
    public ReservaResponse crear(ReservaRequest request) {
        if (!request.getFechaEntrada().isBefore(request.getFechaSalida())) {
            throw new BusinessRuleException("La fecha de entrada debe ser estrictamente anterior a la fecha de salida");
        }

        Cliente cliente = clienteRepository.findById(request.getIdCliente())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + request.getIdCliente()));

        if (!Boolean.TRUE.equals(cliente.getEstado())) {
            throw new BusinessRuleException("No se pueden crear reservas para clientes inactivos");
        }

        LocalDateTime ahora = LocalDateTime.now();

        // 1. Bloqueo Pesimista sobre habitaciones para evitar race condition en creación
        List<Habitacion> habitaciones = habitacionRepository.findAllByIdWithPessimisticLock(request.getHabitacionesIds());
        if (habitaciones.size() != request.getHabitacionesIds().size()) {
            throw new ResourceNotFoundException("Una o más habitaciones no existen en el sistema");
        }

        // 2. Verificar disponibilidad ignorando reservas PENDIENTE que ya expiraron
        if (reservaRepository.existeSolapamiento(request.getHabitacionesIds(), request.getFechaEntrada(), request.getFechaSalida(), ahora, null)) {
            throw new BusinessRuleException("Una o más habitaciones seleccionadas presentan un conflicto con una reserva activa o pendiente no expirada");
        }

        long noches = ChronoUnit.DAYS.between(request.getFechaEntrada(), request.getFechaSalida());
        BigDecimal totalReserva = BigDecimal.ZERO;
        List<ReservaHabitacion> detalles = new ArrayList<>();

        Reserva reserva = Reserva.builder()
                .cliente(cliente)
                .fechaEntrada(request.getFechaEntrada())
                .fechaSalida(request.getFechaSalida())
                .fechaReserva(ahora)
                .fechaExpiracion(ahora.plusMinutes(15)) // Expiración explícita a 15 minutos
                .estado(EstadoReserva.PENDIENTE)
                .total(BigDecimal.ZERO)
                .build();

        for (Habitacion habitacion : habitaciones) {
            if (!"DISPONIBLE".equalsIgnoreCase(habitacion.getEstado())) {
                throw new BusinessRuleException("La habitación " + habitacion.getNumero() + " no está operativa (Estado: " + habitacion.getEstado() + ")");
            }

            BigDecimal precioNoche = habitacion.getTipoHabitacion().getPrecioNoche();
            BigDecimal subtotal = precioNoche.multiply(BigDecimal.valueOf(noches));
            totalReserva = totalReserva.add(subtotal);

            ReservaHabitacion rh = ReservaHabitacion.builder()
                    .reserva(reserva)
                    .habitacion(habitacion)
                    .precioNoche(precioNoche)
                    .subtotal(subtotal)
                    .build();

            detalles.add(rh);
        }

        reserva.setTotal(totalReserva);
        reserva.setReservaHabitaciones(detalles);

        Reserva reservaGuardada = reservaRepository.save(reserva);
        return ReservaMapper.toResponse(reservaGuardada);
    }

    @Override
    @Transactional(readOnly = true)
    public ReservaResponse obtenerPorId(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con ID: " + id));
        return ReservaMapper.toResponse(reserva);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReservaResponse> listarTodas(Pageable pageable) {
        return reservaRepository.findAll(pageable)
                .map(ReservaMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReservaResponse> listarPorCliente(Long idCliente, Pageable pageable) {
        return reservaRepository.findByClienteIdCliente(idCliente, pageable)
                .map(ReservaMapper::toResponse);
    }

    @Override
    @Transactional
    public ReservaResponse modificarFechasOObjetos(Long id, ReservaRequest request) {
        Reserva reserva = reservaRepository.findByIdWithPessimisticLock(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con ID: " + id));

        // 1. Validar estados usando el enum EstadoReserva
        if (reserva.getEstado() == EstadoReserva.CANCELADA || reserva.getEstado() == EstadoReserva.FINALIZADA) {
            throw new BusinessRuleException("No se puede modificar una reserva en estado " + reserva.getEstado());
        }

        // 2. Validar rango de fechas (RN-01)
        if (!request.getFechaEntrada().isBefore(request.getFechaSalida())) {
            throw new BusinessRuleException("La fecha de entrada debe ser strictly anterior a la fecha de salida");
        }

        LocalDateTime ahora = LocalDateTime.now();

        // 3. RN-08: Revalidación de solapamiento pasando el parámetro 'ahora' requerido por el repositorio
        if (reservaRepository.existeSolapamiento(request.getHabitacionesIds(), request.getFechaEntrada(), request.getFechaSalida(), ahora, id)) {
            throw new BusinessRuleException("El nuevo período de reserva genera un conflicto con una reserva existente");
        }

        reserva.getReservaHabitaciones().clear();

        long noches = ChronoUnit.DAYS.between(request.getFechaEntrada(), request.getFechaSalida());
        BigDecimal totalReserva = BigDecimal.ZERO;

        for (Long habitacionId : request.getHabitacionesIds()) {
            Habitacion habitacion = habitacionRepository.findById(habitacionId)
                    .orElseThrow(() -> new ResourceNotFoundException("Habitación no encontrada con ID: " + habitacionId));

            if (!"DISPONIBLE".equalsIgnoreCase(habitacion.getEstado())) {
                throw new BusinessRuleException("La habitación " + habitacion.getNumero() + " no está operativa");
            }

            BigDecimal precioNoche = habitacion.getTipoHabitacion().getPrecioNoche();
            BigDecimal subtotal = precioNoche.multiply(BigDecimal.valueOf(noches));
            totalReserva = totalReserva.add(subtotal);

            ReservaHabitacion rh = ReservaHabitacion.builder()
                    .reserva(reserva)
                    .habitacion(habitacion)
                    .precioNoche(precioNoche)
                    .subtotal(subtotal)
                    .build();

            reserva.getReservaHabitaciones().add(rh);
        }

        reserva.setFechaEntrada(request.getFechaEntrada());
        reserva.setFechaSalida(request.getFechaSalida());
        reserva.setTotal(totalReserva);

        Reserva actualizada = reservaRepository.save(reserva);
        return ReservaMapper.toResponse(actualizada);
    }

    @Override
    @Transactional
    public void cancelar(Long id) {
        Reserva reserva = reservaRepository.findByIdWithPessimisticLock(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con ID: " + id));

        if (reserva.getEstado() == EstadoReserva.CANCELADA) {
            throw new BusinessRuleException("La reserva ya se encuentra cancelada");
        }

        reserva.setEstado(EstadoReserva.CANCELADA);
        reservaRepository.save(reserva);
    }

    @Override
    public Page<ReservaResponse> buscarConFiltros(ReservaFilter filter, Pageable pageable) {
        Specification<Reserva> spec = ReservaSpecification.conFiltros(filter);
        return reservaRepository.findAll(spec, pageable)
                .map(ReservaMapper::toResponse);
    }
}
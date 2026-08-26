package com.hotel.reservas.service.impl;

import com.hotel.reservas.dto.request.PagoRequest;
import com.hotel.reservas.dto.response.PagoResponse;
import com.hotel.reservas.entity.EstadoReserva;
import com.hotel.reservas.entity.Pago;
import com.hotel.reservas.entity.Reserva;
import com.hotel.reservas.exception.BusinessRuleException;
import com.hotel.reservas.exception.ResourceNotFoundException;
import com.hotel.reservas.repository.PagoRepository;
import com.hotel.reservas.repository.ReservaRepository;
import com.hotel.reservas.service.PagoService;
import com.hotel.reservas.util.PagoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PagoServiceImpl implements PagoService {

    private final PagoRepository pagoRepository;
    private final ReservaRepository reservaRepository;

    @Override
    @Transactional
    public PagoResponse registrarPago(PagoRequest request) {
        if (request.getMonto() == null || request.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException("El monto del pago debe ser estrictamente mayor a 0");
        }

        // 1. Adquisición de bloqueo pesimista sobre la Reserva
        Reserva reserva = reservaRepository.findByIdWithPessimisticLock(request.getIdReserva())
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con ID: " + request.getIdReserva()));

        LocalDateTime ahora = LocalDateTime.now();

        // 2. Validaciones estrictas de estado y expiración dentro del bloqueo
        if (reserva.getEstado() == EstadoReserva.CANCELADA) {
            throw new BusinessRuleException("No se puede pagar una reserva CANCELADA");
        }

        if (reserva.getEstado() == EstadoReserva.CONFIRMADA) {
            throw new BusinessRuleException("La reserva ya fue CONFIRMADA previamente");
        }

        if (reserva.getEstado() == EstadoReserva.FINALIZADA) {
            throw new BusinessRuleException("No se puede pagar una reserva FINALIZADA");
        }

        if (reserva.getEstado() == EstadoReserva.PENDIENTE && reserva.getFechaExpiracion().isBefore(ahora)) {
            // Se marca cancelada en caliente si la transacción del pago detecta la expiración primero
            reserva.setEstado(EstadoReserva.CANCELADA);
            reservaRepository.save(reserva);
            throw new BusinessRuleException("La reserva PENDIENTE ha expirado. Por favor, realice una nueva reserva");
        }

        // 3. Registro del Pago y confirmación de la Reserva
        Pago pago = PagoMapper.toEntity(request, reserva);
        Pago guardado = pagoRepository.save(pago);

        reserva.setEstado(EstadoReserva.CONFIRMADA);
        reservaRepository.save(reserva);

        return PagoMapper.toResponse(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public PagoResponse obtenerPorId(Long id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado con ID: " + id));
        return PagoMapper.toResponse(pago);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoResponse> listarPorReserva(Long idReserva) {
        if (!reservaRepository.existsById(idReserva)) {
            throw new ResourceNotFoundException("Reserva no encontrada con ID: " + idReserva);
        }
        return pagoRepository.findByReservaIdReserva(idReserva).stream()
                .map(PagoMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PagoResponse> listarTodos(Pageable pageable) {
        return pagoRepository.findAll(pageable)
                .map(PagoMapper::toResponse);
    }

    @Override
    @Transactional
    public void anularPago(Long id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado con ID: " + id));

        if ("ANULADO".equalsIgnoreCase(pago.getEstado())) {
            throw new BusinessRuleException("El pago ya se encuentra en estado ANULADO");
        }

        pago.setEstado("ANULADO");
        pagoRepository.save(pago);
    }
}
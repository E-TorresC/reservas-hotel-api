package com.hotel.reservas.service.impl;

import com.hotel.reservas.dto.request.PagoRequest;
import com.hotel.reservas.dto.response.PagoResponse;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class PagoServiceImpl implements PagoService {

    private final PagoRepository pagoRepository;
    private final ReservaRepository reservaRepository;

    @Override
    @Transactional
    public PagoResponse registrarPago(PagoRequest request) {
        // RN-10: Validar que el monto sea mayor que 0
        if (request.getMonto() == null || request.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException("El monto del pago debe ser estrictamente mayor a 0");
        }

        Reserva reserva = reservaRepository.findById(request.getIdReserva())
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con ID: " + request.getIdReserva()));

        // RN-10: Validar estado de la reserva para permitir pagos
        if ("CANCELADA".equalsIgnoreCase(reserva.getEstado())) {
            throw new BusinessRuleException("No se pueden registrar pagos para una reserva en estado CANCELADA");
        }

        Pago pago = PagoMapper.toEntity(request, reserva);
        Pago guardado = pagoRepository.save(pago);
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
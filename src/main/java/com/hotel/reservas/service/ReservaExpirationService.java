package com.hotel.reservas.service;

import com.hotel.reservas.entity.EstadoReserva;
import com.hotel.reservas.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservaExpirationService {

    private final ReservaRepository reservaRepository;

    @Transactional(readOnly = true)
    public List<Long> obtenerIdsReservasExpiradas(LocalDateTime ahora) {
        return reservaRepository.findIdsReservasExpiradas(ahora);
    }

    @Transactional
    public void procesarExpiracionIndividual(
            Long idReserva,
            LocalDateTime ahora
    ) {

        reservaRepository.findByIdWithPessimisticLock(idReserva)
                .ifPresent(reserva -> {

                    if (reserva.getEstado() == EstadoReserva.PENDIENTE
                            && !reserva.getFechaExpiracion().isAfter(ahora)) {

                        reserva.setEstado(EstadoReserva.CANCELADA);

                        reservaRepository.save(reserva);

                        log.info(
                                "Reserva ID {} expirada y cambiada a CANCELADA automáticamente",
                                idReserva
                        );
                    }
                });
    }
}
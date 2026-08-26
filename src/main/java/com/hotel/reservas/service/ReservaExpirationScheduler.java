package com.hotel.reservas.service;

import com.hotel.reservas.entity.EstadoReserva;
import com.hotel.reservas.entity.Reserva;
import com.hotel.reservas.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservaExpirationScheduler {

    private final ReservaRepository reservaRepository;

    @Scheduled(fixedDelay = 60000) // Se ejecuta cada minuto
    public void cancelarReservasExpiradas() {
        LocalDateTime ahora = LocalDateTime.now();
        List<Long> idsExpirados = reservaRepository.findIdsReservasExpiradas(ahora);

        if (idsExpirados.isEmpty()) {
            return;
        }

        log.info("Procesando expiración de {} reservas pendientes...", idsExpirados.size());

        for (Long id : idsExpirados) {
            procesarExpiracionIndividual(id, ahora);
        }
    }

    // Aislamiento transaccional individual por registro para evitar bloqueos masivos en la BD
    @Transactional
    public void procesarExpiracionIndividual(Long idReserva, LocalDateTime ahora) {
        reservaRepository.findByIdWithPessimisticLock(idReserva).ifPresent(reserva -> {
            if (reserva.getEstado() == EstadoReserva.PENDIENTE && !reserva.getFechaExpiracion().isAfter(ahora)) {
                reserva.setEstado(EstadoReserva.CANCELADA);
                reservaRepository.save(reserva);
                log.info("Reserva ID {} expirada y cambiada a CANCELADA automáticamente", idReserva);
            }
        });
    }
}
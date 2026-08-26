package com.hotel.reservas.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservaExpirationScheduler {

    private final ReservaExpirationService reservaExpirationService;

    @Scheduled(fixedDelay = 30000)
    public void cancelarReservasExpiradas() {

        LocalDateTime ahora = LocalDateTime.now();

        List<Long> idsExpirados =
                reservaExpirationService.obtenerIdsReservasExpiradas(ahora);

        if (idsExpirados.isEmpty()) {
            return;
        }

        log.info(
                "Procesando expiración de {} reservas pendientes...",
                idsExpirados.size()
        );

        for (Long id : idsExpirados) {
            reservaExpirationService.procesarExpiracionIndividual(id, ahora);
        }
    }
}
package com.hotel.reservas.service.impl;

import com.hotel.reservas.dto.response.DisponibilidadResponse;
import com.hotel.reservas.entity.Habitacion;
import com.hotel.reservas.exception.BusinessRuleException;
import com.hotel.reservas.repository.HabitacionRepository;
import com.hotel.reservas.service.DisponibilidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DisponibilidadServiceImpl implements DisponibilidadService {

    private final HabitacionRepository habitacionRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<DisponibilidadResponse> consultarDisponibilidad(
            LocalDate fechaEntrada,
            LocalDate fechaSalida,
            Long idHotel,
            Long idTipoHabitacion,
            Pageable pageable) {

        if (fechaEntrada == null || fechaSalida == null) {
            throw new BusinessRuleException("Las fechas de entrada y salida son obligatorias para consultar disponibilidad");
        }

        if (!fechaEntrada.isBefore(fechaSalida)) {
            throw new BusinessRuleException("La fecha de entrada debe ser estrictamente anterior a la fecha de salida");
        }

        // Obtener la fecha/hora actual para filtrar reservas PENDIETE expiradas
        LocalDateTime ahora = LocalDateTime.now();

        // CORRECCIÓN AQUÍ: Se añade el argumento 'ahora' antes de 'pageable'
        Page<Habitacion> habitaciones = habitacionRepository.buscarDisponibles(
                fechaEntrada,
                fechaSalida,
                idHotel,
                idTipoHabitacion,
                ahora,
                pageable
        );

        return habitaciones.map(h -> DisponibilidadResponse.builder()
                .idHabitacion(h.getIdHabitacion())
                .numero(h.getNumero())
                .idHotel(h.getHotel().getIdHotel())
                .nombreHotel(h.getHotel().getNombre())
                .idTipoHabitacion(h.getTipoHabitacion().getIdTipoHabitacion())
                .nombreTipoHabitacion(h.getTipoHabitacion().getNombre())
                .capacidad(h.getTipoHabitacion().getCapacidad())
                .precioNoche(h.getTipoHabitacion().getPrecioNoche())
                .estado(h.getEstado())
                .build());
    }
}
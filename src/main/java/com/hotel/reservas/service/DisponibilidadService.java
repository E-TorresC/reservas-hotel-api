package com.hotel.reservas.service;

import com.hotel.reservas.dto.response.DisponibilidadResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface DisponibilidadService {

    Page<DisponibilidadResponse> consultarDisponibilidad(
            LocalDate fechaEntrada,
            LocalDate fechaSalida,
            Long idHotel,
            Long idTipoHabitacion,
            Pageable pageable
    );
}
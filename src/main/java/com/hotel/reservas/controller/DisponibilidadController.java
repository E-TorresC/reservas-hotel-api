package com.hotel.reservas.controller;

import com.hotel.reservas.dto.response.DisponibilidadResponse;
import com.hotel.reservas.service.DisponibilidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/habitaciones/disponibles")
@RequiredArgsConstructor
public class DisponibilidadController {

    private final DisponibilidadService disponibilidadService;

    @GetMapping
    public ResponseEntity<Page<DisponibilidadResponse>> consultarDisponibilidad(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaEntrada,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaSalida,
            @RequestParam(required = false) Long idHotel,
            @RequestParam(required = false) Long idTipoHabitacion,
            @PageableDefault(size = 10) Pageable pageable) {

        Page<DisponibilidadResponse> disponibles = disponibilidadService.consultarDisponibilidad(
                fechaEntrada,
                fechaSalida,
                idHotel,
                idTipoHabitacion,
                pageable
        );
        return ResponseEntity.ok(disponibles);
    }
}
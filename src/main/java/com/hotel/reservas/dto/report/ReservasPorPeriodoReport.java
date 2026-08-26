package com.hotel.reservas.dto.report;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservasPorPeriodoReport {

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Long cantidadReservas;
}
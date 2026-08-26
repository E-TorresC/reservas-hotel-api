package com.hotel.reservas.dto.report;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OcupacionPorTipoHabitacionReport {

    private Long idTipoHabitacion;
    private String nombreTipoHabitacion;
    private Long cantidadHabitaciones;
    private Long totalReservas;
}
package com.hotel.reservas.dto.report;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HabitacionOcupacionReport {

    private Long idHabitacion;
    private String numeroHabitacion;
    private String nombreHotel;
    private String tipoHabitacion;
    private Long totalReservas;
}
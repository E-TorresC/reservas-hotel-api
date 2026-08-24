package com.hotel.reservas.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HabitacionResponse {

    private Long idHabitacion;
    private Long idHotel;
    private String nombreHotel;
    private Long idTipoHabitacion;
    private String nombreTipoHabitacion;
    private String numero;
    private String estado;
    private Long version;
}
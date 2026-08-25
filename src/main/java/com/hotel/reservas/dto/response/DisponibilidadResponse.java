package com.hotel.reservas.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisponibilidadResponse {

    private Long idHabitacion;
    private String numero;
    private Long idHotel;
    private String nombreHotel;
    private Long idTipoHabitacion;
    private String nombreTipoHabitacion;
    private Integer capacidad;
    private BigDecimal precioNoche;
    private String estado;
}
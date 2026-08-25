package com.hotel.reservas.dto.request;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HabitacionFilter {

    private Long idHotel;
    private Long idTipoHabitacion;
    private String numero;
    private String estado;
    private Integer capacidadMinima;
    private BigDecimal precioMaximo;
}
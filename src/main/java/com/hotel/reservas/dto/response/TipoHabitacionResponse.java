package com.hotel.reservas.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoHabitacionResponse {

    private Long idTipoHabitacion;
    private String nombre;
    private String descripcion;
    private Integer capacidad;
    private BigDecimal precioNoche;
    private Boolean estado;
}
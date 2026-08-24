package com.hotel.reservas.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoHabitacionRequest {

    @NotBlank(message = "El nombre del tipo de habitación es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre;

    @Size(max = 250, message = "La descripción no puede superar los 250 caracteres")
    private String descripcion;

    @NotNull(message = "La capacidad es obligatoria")
    @Min(value = 1, message = "La capacidad debe ser al menos de 1 persona")
    private Integer capacidad;

    @NotNull(message = "El precio por noche es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio por noche debe ser mayor a 0")
    private BigDecimal precioNoche;
}
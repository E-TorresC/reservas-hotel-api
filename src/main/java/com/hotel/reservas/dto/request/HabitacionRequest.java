package com.hotel.reservas.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HabitacionRequest {

    @NotNull(message = "El ID del hotel es obligatorio")
    private Long idHotel;

    @NotNull(message = "El ID del tipo de habitación es obligatorio")
    private Long idTipoHabitacion;

    @NotBlank(message = "El número de habitación es obligatorio")
    @Size(max = 20, message = "El número no puede superar los 20 caracteres")
    private String numero;

    @NotBlank(message = "El estado operativo es obligatorio")
    @Pattern(regexp = "^(DISPONIBLE|MANTENIMIENTO|INACTIVA)$",
            message = "El estado debe ser DISPONIBLE, MANTENIMIENTO o INACTIVA")
    private String estado;
}
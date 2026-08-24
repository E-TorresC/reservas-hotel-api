package com.hotel.reservas.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelRequest {

    @NotBlank(message = "El nombre del hotel es obligatorio")
    @Size(max = 150, message = "El nombre no puede superar los 150 caracteres")
    private String nombre;

    @Size(max = 250, message = "La dirección no puede superar los 250 caracteres")
    private String direccion;

    @Size(max = 20, message = "El teléfono no puede superar los 20 caracteres")
    private String telefono;
}
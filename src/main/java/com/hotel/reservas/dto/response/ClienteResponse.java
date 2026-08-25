package com.hotel.reservas.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteResponse {

    private Long idCliente;
    private String nombres;
    private String apellidos;
    private String email;
    private String telefono;
    private Boolean estado;
    private LocalDateTime fechaRegistro;
}
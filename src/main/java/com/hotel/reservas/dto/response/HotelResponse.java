package com.hotel.reservas.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelResponse {

    private Long idHotel;
    private String nombre;
    private String direccion;
    private String telefono;
    private Boolean estado;
}
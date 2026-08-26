package com.hotel.reservas.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagoResponse {

    private Long idPago;
    private Long idReserva;
    private BigDecimal monto;
    private LocalDateTime fechaPago;
    private String metodoPago;
    private String estado;
}
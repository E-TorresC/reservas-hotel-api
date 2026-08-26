package com.hotel.reservas.dto.report;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteMayorGastoReport {

    private Long idCliente;
    private String nombreCompleto;
    private String email;
    private BigDecimal totalGasto;
    private Long cantidadReservas;
}
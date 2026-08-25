package com.hotel.reservas.dto.request;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservaFilter {

    private Long idHotel;
    private Long idTipoHabitacion;
    private String numeroHabitacion;
    private Long idCliente;
    private String estado;
    private LocalDate fechaEntradaDesde;
    private LocalDate fechaEntradaHasta;
    private LocalDate fechaSalidaDesde;
    private LocalDate fechaSalidaHasta;
}
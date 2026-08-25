package com.hotel.reservas.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservaResponse {

    private Long idReserva;
    private Long idCliente;
    private String nombreCliente;
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private LocalDateTime fechaReserva;
    private BigDecimal total;
    private String estado;
    private List<ReservaHabitacionResponse> habitaciones;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ReservaHabitacionResponse {
        private Long idReservaHabitacion;
        private Long idHabitacion;
        private String numeroHabitacion;
        private String tipoHabitacion;
        private BigDecimal precioNoche;
        private BigDecimal subtotal;
    }
}
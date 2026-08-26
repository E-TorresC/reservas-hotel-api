package com.hotel.reservas.dto.report;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngresosPorPeriodoReport {

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private BigDecimal montoTotalIngresos;
    private Long totalPagosRegistrados;
}
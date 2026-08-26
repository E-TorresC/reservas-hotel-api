package com.hotel.reservas.service.impl;

import com.hotel.reservas.dto.report.*;
import com.hotel.reservas.exception.BusinessRuleException;
import com.hotel.reservas.repository.PagoRepository;
import com.hotel.reservas.repository.ReservaRepository;
import com.hotel.reservas.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService {

    private final ReservaRepository reservaRepository;
    private final PagoRepository pagoRepository;

    @Override
    @Transactional(readOnly = true)
    public ReservasPorPeriodoReport obtenerReservasPorPeriodo(LocalDate fechaInicio, LocalDate fechaFin) {
        validarRangoFechas(fechaInicio, fechaFin);

        Long cantidad = reservaRepository.contarReservasPorPeriodo(fechaInicio, fechaFin);

        return ReservasPorPeriodoReport.builder()
                .fechaInicio(fechaInicio)
                .fechaFin(fechaFin)
                .cantidadReservas(cantidad)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<HabitacionOcupacionReport> obtenerHabitacionesConMayorOcupacion() {
        return reservaRepository.obtenerHabitacionesConMayorOcupacion();
    }

    @Override
    @Transactional(readOnly = true)
    public IngresosPorPeriodoReport obtenerIngresosPorPeriodo(LocalDate fechaInicio, LocalDate fechaFin) {
        validarRangoFechas(fechaInicio, fechaFin);

        BigDecimal montoTotal = pagoRepository.sumarIngresosPorPeriodo(fechaInicio, fechaFin);
        Long totalPagos = pagoRepository.contarPagosPorPeriodo(fechaInicio, fechaFin);

        return IngresosPorPeriodoReport.builder()
                .fechaInicio(fechaInicio)
                .fechaFin(fechaFin)
                .montoTotalIngresos(montoTotal)
                .totalPagosRegistrados(totalPagos)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteMayorGastoReport> obtenerClientesConMayorGasto() {
        return pagoRepository.obtenerClientesConMayorGasto();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OcupacionPorTipoHabitacionReport> obtenerOcupacionPorTipoHabitacion() {
        return reservaRepository.obtenerOcupacionPorTipoHabitacion();
    }

    private void validarRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new BusinessRuleException("Las fechas de inicio y fin son obligatorias");
        }
        if (fechaInicio.isAfter(fechaFin)) {
            throw new BusinessRuleException("La fecha de inicio debe ser anterior o igual a la fecha de fin");
        }
    }
}
package com.hotel.reservas.service;

import com.hotel.reservas.dto.report.*;

import java.time.LocalDate;
import java.util.List;

public interface ReporteService {

    ReservasPorPeriodoReport obtenerReservasPorPeriodo(LocalDate fechaInicio, LocalDate fechaFin);

    List<HabitacionOcupacionReport> obtenerHabitacionesConMayorOcupacion();

    IngresosPorPeriodoReport obtenerIngresosPorPeriodo(LocalDate fechaInicio, LocalDate fechaFin);

    List<ClienteMayorGastoReport> obtenerClientesConMayorGasto();

    List<OcupacionPorTipoHabitacionReport> obtenerOcupacionPorTipoHabitacion();
}
package com.hotel.reservas.controller;

import com.hotel.reservas.dto.report.*;
import com.hotel.reservas.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    @GetMapping("/reservas-periodo")
    public ResponseEntity<ReservasPorPeriodoReport> obtenerReservasPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        return ResponseEntity.ok(reporteService.obtenerReservasPorPeriodo(fechaInicio, fechaFin));
    }

    @GetMapping("/habitaciones-mayor-ocupacion")
    public ResponseEntity<List<HabitacionOcupacionReport>> obtenerHabitacionesConMayorOcupacion() {
        return ResponseEntity.ok(reporteService.obtenerHabitacionesConMayorOcupacion());
    }

    @GetMapping("/ingresos-periodo")
    public ResponseEntity<IngresosPorPeriodoReport> obtenerIngresosPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        return ResponseEntity.ok(reporteService.obtenerIngresosPorPeriodo(fechaInicio, fechaFin));
    }

    @GetMapping("/clientes-mayor-gasto")
    public ResponseEntity<List<ClienteMayorGastoReport>> obtenerClientesConMayorGasto() {
        return ResponseEntity.ok(reporteService.obtenerClientesConMayorGasto());
    }

    @GetMapping("/ocupacion-tipo-habitacion")
    public ResponseEntity<List<OcupacionPorTipoHabitacionReport>> obtenerOcupacionPorTipoHabitacion() {
        return ResponseEntity.ok(reporteService.obtenerOcupacionPorTipoHabitacion());
    }
}
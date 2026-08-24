package com.hotel.reservas.controller;

import com.hotel.reservas.dto.request.TipoHabitacionRequest;
import com.hotel.reservas.dto.response.TipoHabitacionResponse;
import com.hotel.reservas.service.TipoHabitacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tipos-habitacion")
@RequiredArgsConstructor
public class TipoHabitacionController {

    private final TipoHabitacionService tipoHabitacionService;

    @PostMapping
    public ResponseEntity<TipoHabitacionResponse> crear(@Valid @RequestBody TipoHabitacionRequest request) {
        TipoHabitacionResponse response = tipoHabitacionService.crear(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoHabitacionResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(tipoHabitacionService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<Page<TipoHabitacionResponse>> listarTodos(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(tipoHabitacionService.listarTodos(pageable));
    }

    @GetMapping("/activos")
    public ResponseEntity<Page<TipoHabitacionResponse>> listarActivos(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(tipoHabitacionService.listarActivos(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoHabitacionResponse> actualizar(@PathVariable Long id, @Valid @RequestBody TipoHabitacionRequest request) {
        return ResponseEntity.ok(tipoHabitacionService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLogicamente(@PathVariable Long id) {
        tipoHabitacionService.eliminarLogicamente(id);
        return ResponseEntity.noContent().build();
    }
}
package com.hotel.reservas.controller;

import com.hotel.reservas.dto.request.HabitacionRequest;
import com.hotel.reservas.dto.response.HabitacionResponse;
import com.hotel.reservas.service.HabitacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/habitaciones")
@RequiredArgsConstructor
public class HabitacionController {

    private final HabitacionService habitacionService;

    @PostMapping
    public ResponseEntity<HabitacionResponse> crear(@Valid @RequestBody HabitacionRequest request) {
        HabitacionResponse response = habitacionService.crear(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HabitacionResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(habitacionService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<Page<HabitacionResponse>> listarTodas(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(habitacionService.listarTodas(pageable));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<Page<HabitacionResponse>> listarPorEstado(
            @PathVariable String estado,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(habitacionService.listarPorEstado(estado, pageable));
    }

    @GetMapping("/hotel/{idHotel}")
    public ResponseEntity<Page<HabitacionResponse>> listarPorHotel(
            @PathVariable Long idHotel,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(habitacionService.listarPorHotel(idHotel, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HabitacionResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody HabitacionRequest request) {
        return ResponseEntity.ok(habitacionService.actualizar(id, request));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(
            @PathVariable Long id,
            @RequestParam String estado) {
        habitacionService.cambiarEstado(id, estado);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLogicamente(@PathVariable Long id) {
        habitacionService.eliminarLogicamente(id);
        return ResponseEntity.noContent().build();
    }
}
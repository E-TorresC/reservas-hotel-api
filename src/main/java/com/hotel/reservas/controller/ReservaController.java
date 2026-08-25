package com.hotel.reservas.controller;

import com.hotel.reservas.dto.request.ReservaFilter;
import com.hotel.reservas.dto.request.ReservaRequest;
import com.hotel.reservas.dto.response.ReservaResponse;
import com.hotel.reservas.service.ReservaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;

    @PostMapping
    public ResponseEntity<ReservaResponse> crear(@Valid @RequestBody ReservaRequest request) {
        ReservaResponse response = reservaService.crear(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<Page<ReservaResponse>> listarTodas(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(reservaService.listarTodas(pageable));
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<Page<ReservaResponse>> listarPorCliente(
            @PathVariable Long idCliente,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(reservaService.listarPorCliente(idCliente, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservaResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ReservaRequest request) {
        return ResponseEntity.ok(reservaService.modificarFechasOObjetos(id, request));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        reservaService.cancelar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/busqueda")
    public ResponseEntity<Page<ReservaResponse>> buscarConFiltros(
            @ModelAttribute ReservaFilter filter,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(reservaService.buscarConFiltros(filter, pageable));
    }
}
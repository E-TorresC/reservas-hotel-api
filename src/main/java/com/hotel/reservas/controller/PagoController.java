package com.hotel.reservas.controller;

import com.hotel.reservas.dto.request.PagoRequest;
import com.hotel.reservas.dto.response.PagoResponse;
import com.hotel.reservas.service.PagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;

    @PostMapping
    public ResponseEntity<PagoResponse> registrarPago(@Valid @RequestBody PagoRequest request) {
        PagoResponse response = pagoService.registrarPago(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.obtenerPorId(id));
    }

    @GetMapping("/reserva/{idReserva}")
    public ResponseEntity<List<PagoResponse>> listarPorReserva(@PathVariable Long idReserva) {
        return ResponseEntity.ok(pagoService.listarPorReserva(idReserva));
    }

    @GetMapping
    public ResponseEntity<Page<PagoResponse>> listarTodos(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(pagoService.listarTodos(pageable));
    }

    @PatchMapping("/{id}/anular")
    public ResponseEntity<Void> anularPago(@PathVariable Long id) {
        pagoService.anularPago(id);
        return ResponseEntity.noContent().build();
    }
}
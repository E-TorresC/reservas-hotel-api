package com.hotel.reservas.controller;

import com.hotel.reservas.dto.request.HotelRequest;
import com.hotel.reservas.dto.response.HotelResponse;
import com.hotel.reservas.service.HotelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hoteles")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @PostMapping
    public ResponseEntity<HotelResponse> crear(@Valid @RequestBody HotelRequest request) {
        HotelResponse response = hotelService.crear(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(hotelService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<Page<HotelResponse>> listarTodos(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(hotelService.listarTodos(pageable));
    }

    @GetMapping("/activos")
    public ResponseEntity<Page<HotelResponse>> listarActivos(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(hotelService.listarActivos(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HotelResponse> actualizar(@PathVariable Long id, @Valid @RequestBody HotelRequest request) {
        return ResponseEntity.ok(hotelService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLogicamente(@PathVariable Long id) {
        hotelService.eliminarLogicamente(id);
        return ResponseEntity.noContent().build();
    }
}
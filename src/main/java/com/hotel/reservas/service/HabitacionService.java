package com.hotel.reservas.service;

import com.hotel.reservas.dto.request.HabitacionRequest;
import com.hotel.reservas.dto.response.HabitacionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HabitacionService {

    HabitacionResponse crear(HabitacionRequest request);

    HabitacionResponse obtenerPorId(Long id);

    Page<HabitacionResponse> listarTodas(Pageable pageable);

    Page<HabitacionResponse> listarPorEstado(String estado, Pageable pageable);

    Page<HabitacionResponse> listarPorHotel(Long idHotel, Pageable pageable);

    HabitacionResponse actualizar(Long id, HabitacionRequest request);

    void cambiarEstado(Long id, String nuevoEstado);

    void eliminarLogicamente(Long id);
}
package com.hotel.reservas.service;

import com.hotel.reservas.dto.request.TipoHabitacionRequest;
import com.hotel.reservas.dto.response.TipoHabitacionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TipoHabitacionService {

    TipoHabitacionResponse crear(TipoHabitacionRequest request);

    TipoHabitacionResponse obtenerPorId(Long id);

    Page<TipoHabitacionResponse> listarTodos(Pageable pageable);

    Page<TipoHabitacionResponse> listarActivos(Pageable pageable);

    TipoHabitacionResponse actualizar(Long id, TipoHabitacionRequest request);

    void eliminarLogicamente(Long id);
}
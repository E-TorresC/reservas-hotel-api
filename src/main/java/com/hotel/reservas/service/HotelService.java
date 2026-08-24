package com.hotel.reservas.service;

import com.hotel.reservas.dto.request.HotelRequest;
import com.hotel.reservas.dto.response.HotelResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HotelService {

    HotelResponse crear(HotelRequest request);

    HotelResponse obtenerPorId(Long id);

    Page<HotelResponse> listarTodos(Pageable pageable);

    Page<HotelResponse> listarActivos(Pageable pageable);

    HotelResponse actualizar(Long id, HotelRequest request);

    void eliminarLogicamente(Long id);
}
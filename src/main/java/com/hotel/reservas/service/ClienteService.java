package com.hotel.reservas.service;

import com.hotel.reservas.dto.request.ClienteRequest;
import com.hotel.reservas.dto.response.ClienteResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClienteService {

    ClienteResponse crear(ClienteRequest request);

    ClienteResponse obtenerPorId(Long id);

    Page<ClienteResponse> listarTodos(Pageable pageable);

    Page<ClienteResponse> listarActivos(Pageable pageable);

    ClienteResponse actualizar(Long id, ClienteRequest request);

    void eliminarLogicamente(Long id);
}
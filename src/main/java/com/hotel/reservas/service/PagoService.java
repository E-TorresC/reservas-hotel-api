package com.hotel.reservas.service;

import com.hotel.reservas.dto.request.PagoRequest;
import com.hotel.reservas.dto.response.PagoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PagoService {

    PagoResponse registrarPago(PagoRequest request);

    PagoResponse obtenerPorId(Long id);

    List<PagoResponse> listarPorReserva(Long idReserva);

    Page<PagoResponse> listarTodos(Pageable pageable);

    void anularPago(Long id);
}
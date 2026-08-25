package com.hotel.reservas.service;

import com.hotel.reservas.dto.request.ReservaFilter;
import com.hotel.reservas.dto.request.ReservaRequest;
import com.hotel.reservas.dto.response.ReservaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReservaService {

    ReservaResponse crear(ReservaRequest request);

    ReservaResponse obtenerPorId(Long id);

    Page<ReservaResponse> listarTodas(Pageable pageable);

    Page<ReservaResponse> listarPorCliente(Long idCliente, Pageable pageable);

    ReservaResponse modificarFechasOObjetos(Long id, ReservaRequest request);

    void cancelar(Long id);

    // NUEVO METODO PARA FILTROS DINÁMICOS CON SPECIFICATIONS
    Page<ReservaResponse> buscarConFiltros(ReservaFilter filter, Pageable pageable);
}
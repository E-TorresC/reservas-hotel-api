package com.hotel.reservas.util.mapper;

import com.hotel.reservas.dto.request.ClienteRequest;
import com.hotel.reservas.dto.response.ClienteResponse;
import com.hotel.reservas.entity.Cliente;

public class ClienteMapper {

    public static Cliente toEntity(ClienteRequest request) {
        return Cliente.builder()
                .nombres(request.getNombres())
                .apellidos(request.getApellidos())
                .email(request.getEmail())
                .telefono(request.getTelefono())
                .estado(true)
                .build();
    }

    public static ClienteResponse toResponse(Cliente entity) {
        return ClienteResponse.builder()
                .idCliente(entity.getIdCliente())
                .nombres(entity.getNombres())
                .apellidos(entity.getApellidos())
                .email(entity.getEmail())
                .telefono(entity.getTelefono())
                .estado(entity.getEstado())
                .fechaRegistro(entity.getFechaRegistro())
                .build();
    }

    public static void updateEntity(Cliente entity, ClienteRequest request) {
        entity.setNombres(request.getNombres());
        entity.setApellidos(request.getApellidos());
        entity.setEmail(request.getEmail());
        entity.setTelefono(request.getTelefono());
    }
}
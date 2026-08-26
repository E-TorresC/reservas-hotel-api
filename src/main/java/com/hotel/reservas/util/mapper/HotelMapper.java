package com.hotel.reservas.util.mapper;

import com.hotel.reservas.dto.request.HotelRequest;
import com.hotel.reservas.dto.response.HotelResponse;
import com.hotel.reservas.entity.Hotel;

public class HotelMapper {

    public static Hotel toEntity(HotelRequest request) {
        return Hotel.builder()
                .nombre(request.getNombre())
                .direccion(request.getDireccion())
                .telefono(request.getTelefono())
                .estado(true)
                .build();
    }

    public static HotelResponse toResponse(Hotel entity) {
        return HotelResponse.builder()
                .idHotel(entity.getIdHotel())
                .nombre(entity.getNombre())
                .direccion(entity.getDireccion())
                .telefono(entity.getTelefono())
                .estado(entity.getEstado())
                .build();
    }

    public static void updateEntity(Hotel entity, HotelRequest request) {
        entity.setNombre(request.getNombre());
        entity.setDireccion(request.getDireccion());
        entity.setTelefono(request.getTelefono());
    }
}
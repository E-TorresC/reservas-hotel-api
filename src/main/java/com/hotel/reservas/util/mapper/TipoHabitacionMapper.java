package com.hotel.reservas.util.mapper;

import com.hotel.reservas.dto.request.TipoHabitacionRequest;
import com.hotel.reservas.dto.response.TipoHabitacionResponse;
import com.hotel.reservas.entity.TipoHabitacion;

public class TipoHabitacionMapper {

    public static TipoHabitacion toEntity(TipoHabitacionRequest request) {
        return TipoHabitacion.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .capacidad(request.getCapacidad())
                .precioNoche(request.getPrecioNoche())
                .estado(true)
                .build();
    }

    public static TipoHabitacionResponse toResponse(TipoHabitacion entity) {
        return TipoHabitacionResponse.builder()
                .idTipoHabitacion(entity.getIdTipoHabitacion())
                .nombre(entity.getNombre())
                .descripcion(entity.getDescripcion())
                .capacidad(entity.getCapacidad())
                .precioNoche(entity.getPrecioNoche())
                .estado(entity.getEstado())
                .build();
    }

    public static void updateEntity(TipoHabitacion entity, TipoHabitacionRequest request) {
        entity.setNombre(request.getNombre());
        entity.setDescripcion(request.getDescripcion());
        entity.setCapacidad(request.getCapacidad());
        entity.setPrecioNoche(request.getPrecioNoche());
    }
}
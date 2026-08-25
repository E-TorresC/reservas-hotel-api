package com.hotel.reservas.mapper;

import com.hotel.reservas.dto.request.HabitacionRequest;
import com.hotel.reservas.dto.response.HabitacionResponse;
import com.hotel.reservas.entity.Habitacion;
import com.hotel.reservas.entity.Hotel;
import com.hotel.reservas.entity.TipoHabitacion;

public class HabitacionMapper {

    public static Habitacion toEntity(HabitacionRequest request, Hotel hotel, TipoHabitacion tipoHabitacion) {
        return Habitacion.builder()
                .hotel(hotel)
                .tipoHabitacion(tipoHabitacion)
                .numero(request.getNumero())
                .estado(request.getEstado())
                .build();
    }

    public static HabitacionResponse toResponse(Habitacion entity) {
        return HabitacionResponse.builder()
                .idHabitacion(entity.getIdHabitacion())
                .idHotel(entity.getHotel().getIdHotel())
                .nombreHotel(entity.getHotel().getNombre())
                .idTipoHabitacion(entity.getTipoHabitacion().getIdTipoHabitacion())
                .nombreTipoHabitacion(entity.getTipoHabitacion().getNombre())
                .numero(entity.getNumero())
                .estado(entity.getEstado())
                .version(entity.getVersion())
                .build();
    }

    public static void updateEntity(Habitacion entity, HabitacionRequest request, Hotel hotel, TipoHabitacion tipoHabitacion) {
        entity.setHotel(hotel);
        entity.setTipoHabitacion(tipoHabitacion);
        entity.setNumero(request.getNumero());
        entity.setEstado(request.getEstado());
    }
}
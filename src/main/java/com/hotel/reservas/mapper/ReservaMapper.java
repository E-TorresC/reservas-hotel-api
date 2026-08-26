package com.hotel.reservas.mapper;

import com.hotel.reservas.dto.response.ReservaResponse;
import com.hotel.reservas.entity.Reserva;

import java.util.stream.Collectors;

public class ReservaMapper {

    public static ReservaResponse toResponse(Reserva entity) {
        return ReservaResponse.builder()
                .idReserva(entity.getIdReserva())
                .idCliente(entity.getCliente().getIdCliente())
                .nombreCliente(entity.getCliente().getNombres() + " " + entity.getCliente().getApellidos())
                .fechaEntrada(entity.getFechaEntrada())
                .fechaSalida(entity.getFechaSalida())
                .fechaReserva(entity.getFechaReserva())
                .total(entity.getTotal())
                // CORRECCIÓN AQUÍ: Convertir enum a String
                .estado(entity.getEstado() != null ? entity.getEstado().name() : null)
                .habitaciones(entity.getReservaHabitaciones().stream()
                        .map(rh -> ReservaResponse.ReservaHabitacionResponse.builder()
                                .idReservaHabitacion(rh.getIdReservaHabitacion())
                                .idHabitacion(rh.getHabitacion().getIdHabitacion())
                                .numeroHabitacion(rh.getHabitacion().getNumero())
                                .tipoHabitacion(rh.getHabitacion().getTipoHabitacion().getNombre())
                                .precioNoche(rh.getPrecioNoche())
                                .subtotal(rh.getSubtotal())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
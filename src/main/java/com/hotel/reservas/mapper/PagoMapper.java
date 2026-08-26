package com.hotel.reservas.util;

import com.hotel.reservas.dto.request.PagoRequest;
import com.hotel.reservas.dto.response.PagoResponse;
import com.hotel.reservas.entity.Pago;
import com.hotel.reservas.entity.Reserva;

public class PagoMapper {

    public static Pago toEntity(PagoRequest request, Reserva reserva) {
        return Pago.builder()
                .reserva(reserva)
                .monto(request.getMonto())
                .metodoPago(request.getMetodoPago())
                .estado("REGISTRADO")
                .build();
    }

    public static PagoResponse toResponse(Pago entity) {
        return PagoResponse.builder()
                .idPago(entity.getIdPago())
                .idReserva(entity.getReserva().getIdReserva())
                .monto(entity.getMonto())
                .fechaPago(entity.getFechaPago())
                .metodoPago(entity.getMetodoPago())
                .estado(entity.getEstado())
                .build();
    }
}
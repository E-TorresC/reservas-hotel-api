package com.hotel.reservas.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagoRequest {

    @NotNull(message = "El ID de la reserva es obligatorio")
    private Long idReserva;

    @NotNull(message = "El monto del pago es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto pagado debe ser mayor a 0")
    private BigDecimal monto;

    @NotBlank(message = "El método de pago es obligatorio")
    @Size(max = 30, message = "El método de pago no puede superar los 30 caracteres")
    private String metodoPago; // TARJETA_CREDITO, TARJETA_DEBITO, TRANSFERENCIA, EFECTIVO
}
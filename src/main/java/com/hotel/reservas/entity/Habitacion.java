package com.hotel.reservas.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "habitacion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_habitacion")
    private Long idHabitacion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_hotel", nullable = false)
    private Hotel hotel;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_tipo_habitacion", nullable = false)
    private TipoHabitacion tipoHabitacion;

    @Column(name = "numero", nullable = false, length = 20)
    private String numero;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado; // DISPONIBLE, MANTENIMIENTO, INACTIVA

    @Version
    @Column(name = "version")
    private Long version;
}
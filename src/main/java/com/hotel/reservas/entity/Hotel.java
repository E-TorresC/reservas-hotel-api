package com.hotel.reservas.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hotel")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_hotel")
    private Long idHotel;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "direccion", length = 250)
    private String direccion;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "estado", nullable = false)
    private Boolean estado = true;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Habitacion> habitaciones = new ArrayList<>();
}
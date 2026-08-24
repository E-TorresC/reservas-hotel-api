package com.hotel.reservas.repository;

import com.hotel.reservas.entity.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    // Obtener hoteles filtrando por estado activo/inactivo con paginación
    Page<Hotel> findByEstado(Boolean estado, Pageable pageable);

    // Obtener hotel por ID validando que esté activo
    Optional<Hotel> findByIdHotelAndEstadoTrue(Long idHotel);
}
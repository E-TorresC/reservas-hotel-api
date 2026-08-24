package com.hotel.reservas.repository;

import com.hotel.reservas.entity.Habitacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {

    Page<Habitacion> findByEstado(String estado, Pageable pageable);

    Page<Habitacion> findByHotelIdHotel(Long idHotel, Pageable pageable);

    boolean existsByHotelIdHotelAndNumero(Long idHotel, String numero);
}
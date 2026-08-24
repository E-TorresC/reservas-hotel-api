package com.hotel.reservas.repository;

import com.hotel.reservas.entity.TipoHabitacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoHabitacionRepository extends JpaRepository<TipoHabitacion, Long> {

    Page<TipoHabitacion> findByEstado(Boolean estado, Pageable pageable);

    Optional<TipoHabitacion> findByIdTipoHabitacionAndEstadoTrue(Long idTipoHabitacion);
}
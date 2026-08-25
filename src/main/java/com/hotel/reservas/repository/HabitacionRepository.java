package com.hotel.reservas.repository;

import com.hotel.reservas.entity.Habitacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Long>, JpaSpecificationExecutor<Habitacion> {

    Page<Habitacion> findByEstado(String estado, Pageable pageable);

    Page<Habitacion> findByHotelIdHotel(Long idHotel, Pageable pageable);

    boolean existsByHotelIdHotelAndNumero(Long idHotel, String numero);

    @Query("""
        SELECT h FROM Habitacion h 
        WHERE h.estado = 'DISPONIBLE' 
        AND (:idHotel IS NULL OR h.hotel.idHotel = :idHotel) 
        AND (:idTipoHabitacion IS NULL OR h.tipoHabitacion.idTipoHabitacion = :idTipoHabitacion) 
        AND h.idHabitacion NOT IN (
            SELECT rh.habitacion.idHabitacion 
            FROM ReservaHabitacion rh 
            JOIN rh.reserva r 
            WHERE r.estado IN ('PENDIENTE', 'CONFIRMADA') 
            AND (:fechaEntrada < r.fechaSalida AND :fechaSalida > r.fechaEntrada)
        )
    """)
    Page<Habitacion> buscarDisponibles(
            @Param("fechaEntrada") LocalDate fechaEntrada,
            @Param("fechaSalida") LocalDate fechaSalida,
            @Param("idHotel") Long idHotel,
            @Param("idTipoHabitacion") Long idTipoHabitacion,
            Pageable pageable
    );
}
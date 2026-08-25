package com.hotel.reservas.repository;

import com.hotel.reservas.entity.Reserva;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long>, JpaSpecificationExecutor<Reserva> {

    Page<Reserva> findByClienteIdCliente(Long idCliente, Pageable pageable);

    // Consulta para detectar solapamiento de fechas en una o varias habitaciones
    @Query("""
        SELECT COUNT(r) > 0 
        FROM Reserva r 
        JOIN r.reservaHabitaciones rh 
        WHERE rh.habitacion.idHabitacion IN :habitacionesIds 
        AND r.estado IN ('PENDIENTE', 'CONFIRMADA') 
        AND (:fechaEntrada < r.fechaSalida AND :fechaSalida > r.fechaEntrada)
        AND (:idReservaExcluir IS NULL OR r.idReserva <> :idReservaExcluir)
    """)
    boolean existeSolapamiento(
            @Param("habitacionesIds") List<Long> habitacionesIds,
            @Param("fechaEntrada") LocalDate fechaEntrada,
            @Param("fechaSalida") LocalDate fechaSalida,
            @Param("idReservaExcluir") Long idReservaExcluir
    );
}
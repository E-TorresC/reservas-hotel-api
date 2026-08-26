package com.hotel.reservas.repository;

import com.hotel.reservas.entity.EstadoReserva;
import com.hotel.reservas.entity.Reserva;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long>, JpaSpecificationExecutor<Reserva> {

    Page<Reserva> findByClienteIdCliente(Long idCliente, Pageable pageable);

    // Bloqueo pesimista individual sobre la reserva para el pago y scheduler
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM Reserva r WHERE r.idReserva = :idReserva")
    Optional<Reserva> findByIdWithPessimisticLock(@Param("idReserva") Long idReserva);

    // Consulta de solapamiento: Considera CONFIRMADA siempre, y PENDIENTE solo si AÚN NO HA EXPIRADO
    @Query("""
        SELECT COUNT(r) > 0 
        FROM Reserva r 
        JOIN r.reservaHabitaciones rh 
        WHERE rh.habitacion.idHabitacion IN :habitacionesIds 
        AND (
            r.estado = com.hotel.reservas.entity.EstadoReserva.CONFIRMADA 
            OR (r.estado = com.hotel.reservas.entity.EstadoReserva.PENDIENTE AND r.fechaExpiracion > :ahora)
        )
        AND (:fechaEntrada < r.fechaSalida AND :fechaSalida > r.fechaEntrada)
        AND (:idReservaExcluir IS NULL OR r.idReserva <> :idReservaExcluir)
    """)
    boolean existeSolapamiento(
            @Param("habitacionesIds") List<Long> habitacionesIds,
            @Param("fechaEntrada") LocalDate fechaEntrada,
            @Param("fechaSalida") LocalDate fechaSalida,
            @Param("ahora") LocalDateTime ahora,
            @Param("idReservaExcluir") Long idReservaExcluir
    );

    // Busca IDs de reservas PENDIENTES cuya expiracion sea menor o igual a la fecha/hora actual
    @Query("""
        SELECT r.idReserva 
        FROM Reserva r 
        WHERE r.estado = com.hotel.reservas.entity.EstadoReserva.PENDIENTE 
        AND r.fechaExpiracion <= :ahora
    """)
    List<Long> findIdsReservasExpiradas(@Param("ahora") LocalDateTime ahora);
}
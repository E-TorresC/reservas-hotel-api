package com.hotel.reservas.repository;

import com.hotel.reservas.dto.report.ClienteMayorGastoReport;
import com.hotel.reservas.entity.Pago;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    List<Pago> findByReservaIdReserva(Long idReserva);

    Page<Pago> findByReservaIdReserva(Long idReserva, Pageable pageable);

    // REP-03: Total de ingresos por rango de fechas de pagos
    @Query("""
        SELECT COALESCE(SUM(p.monto), 0) 
        FROM Pago p 
        WHERE p.estado = 'REGISTRADO' 
        AND CAST(p.fechaPago AS date) BETWEEN :fechaInicio AND :fechaFin
    """)
    BigDecimal sumarIngresosPorPeriodo(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);

    @Query("""
        SELECT COUNT(p) 
        FROM Pago p 
        WHERE p.estado = 'REGISTRADO' 
        AND CAST(p.fechaPago AS date) BETWEEN :fechaInicio AND :fechaFin
    """)
    Long contarPagosPorPeriodo(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);

    // REP-04: Clientes con mayor gasto en reservas
    @Query("""
        SELECT new com.hotel.reservas.dto.report.ClienteMayorGastoReport(
            c.idCliente,
            CONCAT(c.nombres, ' ', c.apellidos),
            c.email,
            SUM(p.monto),
            COUNT(DISTINCT r.idReserva)
        )
        FROM Pago p
        JOIN p.reserva r
        JOIN r.cliente c
        WHERE p.estado = 'REGISTRADO'
        GROUP BY c.idCliente, c.nombres, c.apellidos, c.email
        ORDER BY SUM(p.monto) DESC
    """)
    List<ClienteMayorGastoReport> obtenerClientesConMayorGasto();
}
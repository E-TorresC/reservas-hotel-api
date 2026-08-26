package com.hotel.reservas.repository;

import com.hotel.reservas.entity.Pago;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    List<Pago> findByReservaIdReserva(Long idReserva);

    Page<Pago> findByReservaIdReserva(Long idReserva, Pageable pageable);
}
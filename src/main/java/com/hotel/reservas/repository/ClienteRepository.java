package com.hotel.reservas.repository;

import com.hotel.reservas.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Page<Cliente> findByEstado(Boolean estado, Pageable pageable);

    Optional<Cliente> findByIdClienteAndEstadoTrue(Long idCliente);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdClienteNot(String email, Long idCliente);
}
package com.hotel.reservas.service.impl;

import com.hotel.reservas.dto.request.ClienteRequest;
import com.hotel.reservas.dto.response.ClienteResponse;
import com.hotel.reservas.entity.Cliente;
import com.hotel.reservas.exception.ResourceNotFoundException;
import com.hotel.reservas.repository.ClienteRepository;
import com.hotel.reservas.service.ClienteService;
import com.hotel.reservas.mapper.ClienteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    @Override
    @Transactional
    public ClienteResponse crear(ClienteRequest request) {
        if (clienteRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Ya existe un cliente registrado con el email: " + request.getEmail());
        }

        Cliente cliente = ClienteMapper.toEntity(request);
        Cliente guardado = clienteRepository.save(cliente);
        return ClienteMapper.toResponse(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponse obtenerPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));
        return ClienteMapper.toResponse(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClienteResponse> listarTodos(Pageable pageable) {
        return clienteRepository.findAll(pageable)
                .map(ClienteMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClienteResponse> listarActivos(Pageable pageable) {
        return clienteRepository.findByEstado(true, pageable)
                .map(ClienteMapper::toResponse);
    }

    @Override
    @Transactional
    public ClienteResponse actualizar(Long id, ClienteRequest request) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));

        if (clienteRepository.existsByEmailAndIdClienteNot(request.getEmail(), id)) {
            throw new IllegalArgumentException("El email " + request.getEmail() + " ya pertenece a otro cliente");
        }

        ClienteMapper.updateEntity(cliente, request);
        Cliente actualizado = clienteRepository.save(cliente);
        return ClienteMapper.toResponse(actualizado);
    }

    @Override
    @Transactional
    public void eliminarLogicamente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));

        cliente.setEstado(false);
        clienteRepository.save(cliente);
    }
}
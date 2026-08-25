package com.hotel.reservas.service.impl;

import com.hotel.reservas.dto.request.TipoHabitacionRequest;
import com.hotel.reservas.dto.response.TipoHabitacionResponse;
import com.hotel.reservas.entity.TipoHabitacion;
import com.hotel.reservas.exception.ResourceNotFoundException;
import com.hotel.reservas.repository.TipoHabitacionRepository;
import com.hotel.reservas.service.TipoHabitacionService;
import com.hotel.reservas.mapper.TipoHabitacionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TipoHabitacionServiceImpl implements TipoHabitacionService {

    private final TipoHabitacionRepository tipoHabitacionRepository;

    @Override
    @Transactional
    public TipoHabitacionResponse crear(TipoHabitacionRequest request) {
        TipoHabitacion tipo = TipoHabitacionMapper.toEntity(request);
        TipoHabitacion guardado = tipoHabitacionRepository.save(tipo);
        return TipoHabitacionMapper.toResponse(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public TipoHabitacionResponse obtenerPorId(Long id) {
        TipoHabitacion tipo = tipoHabitacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de habitación no encontrado con ID: " + id));
        return TipoHabitacionMapper.toResponse(tipo);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TipoHabitacionResponse> listarTodos(Pageable pageable) {
        return tipoHabitacionRepository.findAll(pageable)
                .map(TipoHabitacionMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TipoHabitacionResponse> listarActivos(Pageable pageable) {
        return tipoHabitacionRepository.findByEstado(true, pageable)
                .map(TipoHabitacionMapper::toResponse);
    }

    @Override
    @Transactional
    public TipoHabitacionResponse actualizar(Long id, TipoHabitacionRequest request) {
        TipoHabitacion tipo = tipoHabitacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de habitación no encontrado con ID: " + id));

        TipoHabitacionMapper.updateEntity(tipo, request);
        TipoHabitacion actualizado = tipoHabitacionRepository.save(tipo);
        return TipoHabitacionMapper.toResponse(actualizado);
    }

    @Override
    @Transactional
    public void eliminarLogicamente(Long id) {
        TipoHabitacion tipo = tipoHabitacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de habitación no encontrado con ID: " + id));

        tipo.setEstado(false);
        tipoHabitacionRepository.save(tipo);
    }
}
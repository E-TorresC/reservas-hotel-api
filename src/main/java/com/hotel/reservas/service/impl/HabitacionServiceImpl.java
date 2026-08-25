package com.hotel.reservas.service.impl;

import com.hotel.reservas.dto.request.HabitacionRequest;
import com.hotel.reservas.dto.response.HabitacionResponse;
import com.hotel.reservas.entity.Habitacion;
import com.hotel.reservas.entity.Hotel;
import com.hotel.reservas.entity.TipoHabitacion;
import com.hotel.reservas.exception.ResourceNotFoundException;
import com.hotel.reservas.repository.HabitacionRepository;
import com.hotel.reservas.repository.HotelRepository;
import com.hotel.reservas.repository.TipoHabitacionRepository;
import com.hotel.reservas.service.HabitacionService;
import com.hotel.reservas.mapper.HabitacionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HabitacionServiceImpl implements HabitacionService {

    private final HabitacionRepository habitacionRepository;
    private final HotelRepository hotelRepository;
    private final TipoHabitacionRepository tipoHabitacionRepository;

    @Override
    @Transactional
    public HabitacionResponse crear(HabitacionRequest request) {
        Hotel hotel = hotelRepository.findByIdHotelAndEstadoTrue(request.getIdHotel())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel no encontrado o inactivo con ID: " + request.getIdHotel()));

        TipoHabitacion tipoHabitacion = tipoHabitacionRepository.findByIdTipoHabitacionAndEstadoTrue(request.getIdTipoHabitacion())
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de habitación no encontrado o inactivo con ID: " + request.getIdTipoHabitacion()));

        if (habitacionRepository.existsByHotelIdHotelAndNumero(request.getIdHotel(), request.getNumero())) {
            throw new IllegalArgumentException("Ya existe una habitación con el número " + request.getNumero() + " en este hotel");
        }

        Habitacion habitacion = HabitacionMapper.toEntity(request, hotel, tipoHabitacion);
        Habitacion guardada = habitacionRepository.save(habitacion);
        return HabitacionMapper.toResponse(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public HabitacionResponse obtenerPorId(Long id) {
        Habitacion habitacion = habitacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Habitación no encontrada con ID: " + id));
        return HabitacionMapper.toResponse(habitacion);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HabitacionResponse> listarTodas(Pageable pageable) {
        return habitacionRepository.findAll(pageable)
                .map(HabitacionMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HabitacionResponse> listarPorEstado(String estado, Pageable pageable) {
        return habitacionRepository.findByEstado(estado, pageable)
                .map(HabitacionMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HabitacionResponse> listarPorHotel(Long idHotel, Pageable pageable) {
        return habitacionRepository.findByHotelIdHotel(idHotel, pageable)
                .map(HabitacionMapper::toResponse);
    }

    @Override
    @Transactional
    public HabitacionResponse actualizar(Long id, HabitacionRequest request) {
        Habitacion habitacion = habitacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Habitación no encontrada con ID: " + id));

        Hotel hotel = hotelRepository.findByIdHotelAndEstadoTrue(request.getIdHotel())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel no encontrado o inactivo con ID: " + request.getIdHotel()));

        TipoHabitacion tipoHabitacion = tipoHabitacionRepository.findByIdTipoHabitacionAndEstadoTrue(request.getIdTipoHabitacion())
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de habitación no encontrado o inactivo con ID: " + request.getIdTipoHabitacion()));

        HabitacionMapper.updateEntity(habitacion, request, hotel, tipoHabitacion);
        Habitacion actualizada = habitacionRepository.save(habitacion);
        return HabitacionMapper.toResponse(actualizada);
    }

    @Override
    @Transactional
    public void cambiarEstado(Long id, String nuevoEstado) {
        Habitacion habitacion = habitacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Habitación no encontrada con ID: " + id));

        habitacion.setEstado(nuevoEstado);
        habitacionRepository.save(habitacion);
    }

    @Override
    @Transactional
    public void eliminarLogicamente(Long id) {
        cambiarEstado(id, "INACTIVA");
    }
}
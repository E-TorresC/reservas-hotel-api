package com.hotel.reservas.service.impl;

import com.hotel.reservas.dto.request.HotelRequest;
import com.hotel.reservas.dto.response.HotelResponse;
import com.hotel.reservas.entity.Hotel;
import com.hotel.reservas.exception.ResourceNotFoundException;
import com.hotel.reservas.repository.HotelRepository;
import com.hotel.reservas.service.HotelService;
import com.hotel.reservas.util.HotelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;

    @Override
    @Transactional
    public HotelResponse crear(HotelRequest request) {
        Hotel hotel = HotelMapper.toEntity(request);
        Hotel hotelGuardado = hotelRepository.save(hotel);
        return HotelMapper.toResponse(hotelGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public HotelResponse obtenerPorId(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel no encontrado con el ID: " + id));
        return HotelMapper.toResponse(hotel);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HotelResponse> listarTodos(Pageable pageable) {
        return hotelRepository.findAll(pageable)
                .map(HotelMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HotelResponse> listarActivos(Pageable pageable) {
        return hotelRepository.findByEstado(true, pageable)
                .map(HotelMapper::toResponse);
    }

    @Override
    @Transactional
    public HotelResponse actualizar(Long id, HotelRequest request) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel no encontrado con el ID: " + id));

        HotelMapper.updateEntity(hotel, request);
        Hotel hotelActualizado = hotelRepository.save(hotel);
        return HotelMapper.toResponse(hotelActualizado);
    }

    @Override
    @Transactional
    public void eliminarLogicamente(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel no encontrado con el ID: " + id));

        hotel.setEstado(false);
        hotelRepository.save(hotel);
    }
}
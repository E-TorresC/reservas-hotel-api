package com.hotel.reservas.repository.specification;

import com.hotel.reservas.dto.request.HabitacionFilter;
import com.hotel.reservas.entity.Habitacion;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class HabitacionSpecification {

    public static Specification<Habitacion> conFiltros(HabitacionFilter filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getIdHotel() != null) {
                predicates.add(criteriaBuilder.equal(root.get("hotel").get("idHotel"), filter.getIdHotel()));
            }

            if (filter.getIdTipoHabitacion() != null) {
                predicates.add(criteriaBuilder.equal(root.get("tipoHabitacion").get("idTipoHabitacion"), filter.getIdTipoHabitacion()));
            }

            if (StringUtils.hasText(filter.getNumero())) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("numero")), "%" + filter.getNumero().toLowerCase() + "%"));
            }

            if (StringUtils.hasText(filter.getEstado())) {
                predicates.add(criteriaBuilder.equal(root.get("estado"), filter.getEstado()));
            }

            if (filter.getCapacidadMinima() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("tipoHabitacion").get("capacidad"), filter.getCapacidadMinima()));
            }

            if (filter.getPrecioMaximo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("tipoHabitacion").get("precioNoche"), filter.getPrecioMaximo()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
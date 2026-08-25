package com.hotel.reservas.repository.specification;

import com.hotel.reservas.dto.request.ReservaFilter;
import com.hotel.reservas.entity.Habitacion;
import com.hotel.reservas.entity.Reserva;
import com.hotel.reservas.entity.ReservaHabitacion;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ReservaSpecification {

    public static Specification<Reserva> conFiltros(ReservaFilter filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Evitar duplicados en el Count Query de Paginación cuando hay JOINs
            if (query != null && Long.class.equals(query.getResultType())) {
                query.distinct(true);
            } else if (query != null) {
                query.distinct(true);
            }

            if (filter.getIdCliente() != null) {
                predicates.add(criteriaBuilder.equal(root.get("cliente").get("idCliente"), filter.getIdCliente()));
            }

            if (StringUtils.hasText(filter.getEstado())) {
                predicates.add(criteriaBuilder.equal(root.get("estado"), filter.getEstado()));
            }

            if (filter.getFechaEntradaDesde() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("fechaEntrada"), filter.getFechaEntradaDesde()));
            }

            if (filter.getFechaEntradaHasta() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("fechaEntrada"), filter.getFechaEntradaHasta()));
            }

            if (filter.getFechaSalidaDesde() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("fechaSalida"), filter.getFechaSalidaDesde()));
            }

            if (filter.getFechaSalidaHasta() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("fechaSalida"), filter.getFechaSalidaHasta()));
            }

            // Filtros que requieren JOIN con ReservaHabitacion y Habitacion
            if (filter.getIdHotel() != null || filter.getIdTipoHabitacion() != null || StringUtils.hasText(filter.getNumeroHabitacion())) {
                Join<Reserva, ReservaHabitacion> joinDetalle = root.join("reservaHabitaciones", JoinType.INNER);
                Join<ReservaHabitacion, Habitacion> joinHabitacion = joinDetalle.join("habitacion", JoinType.INNER);

                if (filter.getIdHotel() != null) {
                    predicates.add(criteriaBuilder.equal(joinHabitacion.get("hotel").get("idHotel"), filter.getIdHotel()));
                }

                if (filter.getIdTipoHabitacion() != null) {
                    predicates.add(criteriaBuilder.equal(joinHabitacion.get("tipoHabitacion").get("idTipoHabitacion"), filter.getIdTipoHabitacion()));
                }

                if (StringUtils.hasText(filter.getNumeroHabitacion())) {
                    predicates.add(criteriaBuilder.equal(joinHabitacion.get("numero"), filter.getNumeroHabitacion()));
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
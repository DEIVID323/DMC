package com.example.DMC.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.DMC.enums.EstadoVenta;
import com.example.DMC.model.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer> {
    long countByEstado(EstadoVenta estado);

    long countByFechaVentaBetweenAndEstado(
            LocalDateTime start,
            LocalDateTime end,
            EstadoVenta estado);
}

package com.example.DMC.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    @Query("SELECT v FROM Venta v ORDER BY v.fechaVenta DESC")
    List<Venta> findAllOrderByFechaDesc();

    List<Venta> findByEstadoOrderByFechaVentaDesc(EstadoVenta estado);
}

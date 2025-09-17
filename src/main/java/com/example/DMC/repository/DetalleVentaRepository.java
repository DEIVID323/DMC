package com.example.DMC.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.DMC.model.DetalleVenta;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Integer> {
}
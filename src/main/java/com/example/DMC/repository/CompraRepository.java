package com.example.DMC.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.DMC.model.Compra;

public interface CompraRepository extends JpaRepository<Compra, Integer> {
    long countByEstado(String estado);
}
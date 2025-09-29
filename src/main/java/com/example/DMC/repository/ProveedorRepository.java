package com.example.DMC.repository;

import com.example.DMC.model.Proveedor;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;



public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {
    List<Proveedor> findByActivoTrue();
    List<Proveedor> findByActivoFalse();
}

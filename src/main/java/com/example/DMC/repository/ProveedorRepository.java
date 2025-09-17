package com.example.DMC.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.DMC.model.Proveedores.Proveedor;

public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {
}
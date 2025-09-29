package com.example.DMC.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.DMC.model.Almacen;

public interface AlmacenRepository extends JpaRepository<Almacen, Integer> {
    List<Almacen> findByActivoTrue();
}

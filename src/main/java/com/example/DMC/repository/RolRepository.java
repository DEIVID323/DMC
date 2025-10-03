package com.example.DMC.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.DMC.model.Rol;


public interface RolRepository extends JpaRepository<Rol, Integer> {
    boolean existsByNombreRolIgnoreCase(String nombreRol);
}
package com.example.DMC.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.DMC.model.RolPermiso;

public interface RolPermisoRepository extends JpaRepository<RolPermiso, RolPermisoId> {
    List<RolPermiso> findByIdRol(Integer idRol); // Add this (note: idRol is part of the composite key)
}
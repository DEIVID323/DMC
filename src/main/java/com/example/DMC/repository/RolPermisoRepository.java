package com.example.DMC.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.DMC.model.RolPermiso;
import com.example.DMC.model.RolPermisoId;

public interface RolPermisoRepository extends JpaRepository<RolPermiso, RolPermisoId> {
}
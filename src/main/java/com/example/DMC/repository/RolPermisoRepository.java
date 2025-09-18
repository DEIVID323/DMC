package com.example.DMC.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.DMC.model.RolPermiso;
import com.example.DMC.model.RolPermisoId;

import jakarta.persistence.criteria.CriteriaBuilder.In;

@Repository
public interface RolPermisoRepository extends JpaRepository<RolPermiso, RolPermisoId> {


    List<RolPermiso> findByRolIdRol(Integer idRol);
}
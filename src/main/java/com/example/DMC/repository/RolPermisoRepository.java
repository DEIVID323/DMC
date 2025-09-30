package com.example.DMC.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.DMC.model.RolPermiso;
import com.example.DMC.model.RolPermisoId;

import jakarta.persistence.criteria.CriteriaBuilder.In;

@Repository
public interface RolPermisoRepository extends JpaRepository<RolPermiso, RolPermisoId> {
    
    @Query(value = """
            SELECT p.nombre_permiso
            FROM usuarios u
            JOIN roles r        ON r.id_rol = u.id_rol
            JOIN rol_permiso rp ON rp.id_rol = r.id_rol
            JOIN permisos p     ON p.id_permiso = rp.id_permiso
            WHERE u.username = :username
            """, nativeQuery = true)
    List<String> findPermisosByUsername(@Param("username") String username);
}


package com.example.DMC.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.DMC.model.Permiso;

public interface PermisoRepository extends JpaRepository<Permiso, Integer> {    
    
    Permiso findByNombrePermiso(String nombrePermiso);
   
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
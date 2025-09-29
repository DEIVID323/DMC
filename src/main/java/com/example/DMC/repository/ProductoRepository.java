package com.example.DMC.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.DMC.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
        List<Producto> findByActivoTrue();

    long countByActivo(boolean activo);

    long countByStockLessThanAndActivoTrue(int stock);


    // ✅ Método corregido que usa el nombre real de la propiedad
    List<Producto> findByNombreContainingIgnoreCaseOrCodigoBarraContainingIgnoreCase(
            String nombre, String codigoBarra);

    // Buscar productos activos con stock mayor a X
    List<Producto> findByActivoTrueAndStockGreaterThan(int stock);

    // Buscar por categoría activa
    List<Producto> findByActivoTrueAndCategoriaActivoTrue();

    // ✅ MANTÉN ESTE - Método con @Query personalizada
    @Query("""
            SELECT p FROM Producto p
            WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :q, '%'))
               OR LOWER(p.codigoBarra) LIKE LOWER(CONCAT('%', :q, '%'))
            """)
    List<Producto> searchByNombreOrCodigo(@Param("q") String q);
    


}
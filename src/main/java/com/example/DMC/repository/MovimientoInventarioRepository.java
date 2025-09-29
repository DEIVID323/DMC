package com.example.DMC.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.DMC.model.MovimientoInventario;
import com.example.DMC.enums.TipoMovimientoInventario;

import java.util.List;

@Repository
public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Integer> {
    List<MovimientoInventario> findByOrderByFechaDesc();

    List<MovimientoInventario> findByProductoIdProductoOrderByFechaDesc(Integer idProducto);

    List<MovimientoInventario> findByTipoMovimientoOrderByFechaDesc(TipoMovimientoInventario tipoMovimiento);

    @Query("SELECT m FROM MovimientoInventario m WHERE m.producto.idProducto = :idProducto AND m.almacen.id = :idAlmacen ORDER BY m.fecha DESC")
    List<MovimientoInventario> findByProductoAndAlmacenOrderByFechaDesc(@Param("idProducto") Integer idProducto,
            @Param("idAlmacen") Integer idAlmacen);
}

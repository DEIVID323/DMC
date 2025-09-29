package com.example.DMC.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.DMC.enums.TipoMovimiento;
import com.example.DMC.model.MovimientoCaja;

public interface MovimientoCajaRepository extends JpaRepository<MovimientoCaja, Integer> {

    @Query("SELECT m FROM MovimientoCaja m WHERE m.turno.idTurno = :idTurno ORDER BY m.fecha DESC")
    List<MovimientoCaja> findByIdTurno(@Param("idTurno") Integer idTurno);

    // COALESCE para evitar null cuando no hay registros:
    @Query("""
           SELECT COALESCE(SUM(m.monto), 0)
           FROM MovimientoCaja m
           WHERE m.turno.idTurno = :idTurno AND m.tipoMovimiento = :tipo
           """)
    BigDecimal totalPorTipo(@Param("idTurno") Integer idTurno, @Param("tipo") TipoMovimiento tipo);
}

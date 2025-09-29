package com.example.DMC.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.DMC.enums.EstadoTurno;
import com.example.DMC.model.TurnoCaja;

public interface TurnoCajaRepository extends JpaRepository<TurnoCaja, Integer> {
     TurnoCaja findByUsuarioIdUsuarioAndEstado(Integer idUsuario, EstadoTurno estado);
    
    List<TurnoCaja> findByUsuarioIdUsuarioOrderByFechaAperturaDesc(Integer idUsuario);
}

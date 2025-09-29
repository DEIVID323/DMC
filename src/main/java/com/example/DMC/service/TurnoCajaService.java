package com.example.DMC.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DMC.enums.EstadoTurno;
import com.example.DMC.model.TurnoCaja;
import com.example.DMC.repository.TurnoCajaRepository;

@Service
public class TurnoCajaService {
    @Autowired
    private TurnoCajaRepository repository;

    public List<TurnoCaja> findAll() {
        return repository.findAll();
    }

    public Optional<TurnoCaja> findById(Integer id) {
        return repository.findById(id);
    }

    public TurnoCaja save(TurnoCaja entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    // Buscar turno abierto por usuario
    public TurnoCaja findTurnoAbiertoByUsuario(Integer idUsuario) {
        return repository.findByUsuarioIdUsuarioAndEstado(idUsuario, EstadoTurno.abierto);
    }

    // Buscar todos los turnos de un usuario
    public List<TurnoCaja> findByUsuario(Integer idUsuario) {
        return repository.findByUsuarioIdUsuarioOrderByFechaAperturaDesc(idUsuario);
    }

    // Calcular monto final del sistema
    public BigDecimal calcularMontoFinalSistema(TurnoCaja turno) {
        // Aquí deberías calcular: monto_inicial + total_ventas - total_gastos
        // Por ahora retorno solo el monto inicial como ejemplo
        // TODO: Integrar con ventas y gastos
        return turno.getMontoInicial();
    }
}
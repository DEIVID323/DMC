package com.example.DMC.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DMC.enums.TipoMovimiento;
import com.example.DMC.model.MovimientoCaja;
import com.example.DMC.repository.MovimientoCajaRepository;

@Service
public class MovimientoCajaService {
    @Autowired
    private MovimientoCajaRepository repository;

    public List<MovimientoCaja> findAll() {
        return repository.findAll();
    }

    public Optional<MovimientoCaja> findById(Integer id) {
        return repository.findById(id);
    }

    public MovimientoCaja save(MovimientoCaja entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);

    }
    public List<MovimientoCaja> findByIdTurno(Integer idTurno) {
        return repository.findByIdTurno(idTurno);
} 


    public BigDecimal totalPorTipo(Integer idTurno, TipoMovimiento tipo) {
        BigDecimal r = repository.totalPorTipo(idTurno, tipo);
        return r != null ? r : BigDecimal.ZERO;
    }
}

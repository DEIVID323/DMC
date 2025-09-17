package com.example.DMC.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DMC.model.MovimientoInventario;
import com.example.DMC.repository.MovimientoInventarioRepository;

@Service
public class MovimientoInventarioService {
    @Autowired
    private MovimientoInventarioRepository repository;

    public List<MovimientoInventario> findAll() {
        return repository.findAll();
    }

    public Optional<MovimientoInventario> findById(Integer id) {
        return repository.findById(id);
    }

    public MovimientoInventario save(MovimientoInventario entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
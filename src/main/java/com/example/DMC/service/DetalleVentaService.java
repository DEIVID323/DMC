package com.example.DMC.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DMC.model.DetalleVenta;
import com.example.DMC.repository.DetalleVentaRepository;

@Service
public class DetalleVentaService {
    @Autowired
    private DetalleVentaRepository repository;

    public List<DetalleVenta> findAll() {
        return repository.findAll();
    }

    public Optional<DetalleVenta> findById(Integer id) {
        return repository.findById(id);
    }

    public DetalleVenta save(DetalleVenta entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
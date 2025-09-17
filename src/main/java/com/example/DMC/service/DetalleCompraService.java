package com.example.DMC.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DMC.model.DetalleCompra;
import com.example.DMC.repository.DetalleCompraRepository;

@Service
public class DetalleCompraService {
    @Autowired
    private DetalleCompraRepository repository;

    public List<DetalleCompra> findAll() {
        return repository.findAll();
    }

    public Optional<DetalleCompra> findById(Integer id) {
        return repository.findById(id);
    }

    public DetalleCompra save(DetalleCompra entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}

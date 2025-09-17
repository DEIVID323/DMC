package com.example.DMC.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DMC.model.Proveedores.Proveedor;
import com.example.DMC.repository.ProveedorRepository;

@Service
public class ProveedorService {
    @Autowired
    private ProveedorRepository repository;

    public List<Proveedor> findAll() {
        return repository.findAll();
    }

    public Optional<Proveedor> findById(Integer id) {
        return repository.findById(id);
    }

    public Proveedor save(Proveedor entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
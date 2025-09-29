package com.example.DMC.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DMC.model.Producto;
import com.example.DMC.repository.ProductoRepository;

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository repository;

    public List<Producto> findAll() {
        return repository.findAll();
    }

    public Optional<Producto> findById(Integer id) {
        return repository.findById(id);
    }

    public Producto save(Producto entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    // MANTÉN SOLO UNO DE ESTOS MÉTODOS:
    public List<Producto> buscarPorNombreOCodigo(String term) {
        return repository.searchByNombreOrCodigo(term);
    }
    public List<Producto> findByActivoTrue() {
        return repository.findByActivoTrue();

    }
    

    
}
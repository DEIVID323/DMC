package com.example.DMC.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DMC.model.Categoria;
import com.example.DMC.repository.CategoriaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository repository;

    public List<Categoria> findAll() {
        return repository.findAll();
    }

    public List<Categoria> findByActivoTrue() {
        return repository.findByActivoTrue();
    }

    public List<Categoria> findByActivoFalse() {
        return repository.findByActivoFalse();
    }

    public Optional<Categoria> findById(Integer id) {
        return repository.findById(id);
    }

    public Categoria save(Categoria entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
    
}

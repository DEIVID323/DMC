package com.example.DMC.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DMC.model.Compra;
import com.example.DMC.repository.CompraRepository;

@Service
public class CompraService {
    @Autowired
    private CompraRepository repository;

    public List<Compra> findAll() {
        return repository.findAll();
    }

    public Optional<Compra> findById(Integer id) {
        return repository.findById(id);
    }

    public Compra save(Compra entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}

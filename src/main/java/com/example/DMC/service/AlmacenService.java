package com.example.DMC.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DMC.model.Almacen;
import com.example.DMC.repository.AlmacenRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AlmacenService {
    @Autowired
    private AlmacenRepository repository;

    public List<Almacen> findAll() {
        return repository.findAll();
    }

    public Optional<Almacen> findById(Integer id) {
        return repository.findById(id);
    }

    public Almacen save(Almacen entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}

package com.example.DMC.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DMC.model.Permiso;
import com.example.DMC.repository.PermisoRepository;

@Service
public class PermisoService {
    @Autowired
    private PermisoRepository repository;

    public List<Permiso> findAll() {
        return repository.findAll();
    }

    public Optional<Permiso> findById(Integer id) {
        return repository.findById(id);
    }

    public Permiso save(Permiso entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
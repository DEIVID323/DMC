package com.example.DMC.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DMC.model.RolPermiso;
import com.example.DMC.model.RolPermisoId;
import com.example.DMC.repository.RolPermisoRepository;

@Service
public class RolPermisoService {
    @Autowired
    private RolPermisoRepository repository;

    public List<RolPermiso> findAll() {
        return repository.findAll();
    }

    public Optional<RolPermiso> findById(RolPermisoId id) {
        return repository.findById(id);
    }

    public RolPermiso save(RolPermiso entity) {
        return repository.save(entity);
    }

    public void deleteById(RolPermisoId id) {
        repository.deleteById(id);
    }
}
package com.example.DMC.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DMC.model.Rol;
import com.example.DMC.repository.RolRepository;

@Service
public class RolService {
    @Autowired
    private RolRepository repository;

    public List<Rol> findAll() { return repository.findAll(); }
    public Optional<Rol> findById(Integer id) { return repository.findById(id); }
    public Rol save(Rol entity) { return repository.save(entity); }
    public void deleteById(Integer id) { repository.deleteById(id); }
}

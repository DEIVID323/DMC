package com.example.DMC.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DMC.model.Cliente;
import com.example.DMC.repository.ClienteRepository;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository repository;

    public List<Cliente> findAll() {
        return repository.findAll();
    }

    public Optional<Cliente> findById(Integer id) {
        return repository.findById(id);
    }

    public Cliente save(Cliente entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}

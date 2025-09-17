package com.example.DMC.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DMC.model.TipoMoneda;
import com.example.DMC.repository.TipoMonedaRepository;

@Service
public class TipoMonedaService {
    @Autowired
    private TipoMonedaRepository repository;

    public List<TipoMoneda> findAll() {
        return repository.findAll();
    }

    public Optional<TipoMoneda> findById(Integer id) {
        return repository.findById(id);
    }

    public TipoMoneda save(TipoMoneda entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
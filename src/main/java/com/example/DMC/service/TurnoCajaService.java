package com.example.DMC.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DMC.model.TurnoCaja;
import com.example.DMC.repository.TurnoCajaRepository;

@Service
public class TurnoCajaService {
    @Autowired
    private TurnoCajaRepository repository;

    public List<TurnoCaja> findAll() {
        return repository.findAll();
    }

    public Optional<TurnoCaja> findById(Integer id) {
        return repository.findById(id);
    }

    public TurnoCaja save(TurnoCaja entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
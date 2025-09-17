package com.example.DMC.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DMC.model.TipoCambio;
import com.example.DMC.model.TipoCambioId;
import com.example.DMC.repository.TipoCambioRepository;

@Service
public class TipoCambioService {
    @Autowired
    private TipoCambioRepository repository;

    public List<TipoCambio> findAll() {
        return repository.findAll();
    }

    public Optional<TipoCambio> findById(TipoCambioId id) {
        return repository.findById(id);
    }

    public TipoCambio save(TipoCambio entity) {
        return repository.save(entity);
    }

    public void deleteById(TipoCambioId id) {
        repository.deleteById(id);
    }
}
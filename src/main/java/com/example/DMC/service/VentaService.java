package com.example.DMC.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DMC.enums.EstadoVenta;
import com.example.DMC.model.Venta;
import com.example.DMC.repository.VentaRepository;

@Service
public class VentaService {
    @Autowired
    private VentaRepository repository;

    public List<Venta> findAll() {
        return repository.findAll();
    }

    public List<Venta> findAllOrderByFechaDesc() {
        return repository.findAllOrderByFechaDesc();
    }

    public List<Venta> findByEstado(EstadoVenta estado) {
        return repository.findByEstadoOrderByFechaVentaDesc(estado);
    }

    public Optional<Venta> findById(Integer id) {
        return repository.findById(id);
    }

    public Venta save(Venta entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

}

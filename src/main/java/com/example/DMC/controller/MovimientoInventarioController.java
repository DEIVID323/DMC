package com.example.DMC.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.DMC.model.MovimientoInventario;
import com.example.DMC.service.MovimientoInventarioService;

@RestController
@RequestMapping("/api/movimientos-inventario")
public class MovimientoInventarioController {
    @Autowired
    private MovimientoInventarioService service;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ALMACENISTA')")
    public List<MovimientoInventario> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ALMACENISTA')")
    public ResponseEntity<MovimientoInventario> getById(@PathVariable Integer id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ALMACENISTA')")
    public MovimientoInventario create(@RequestBody MovimientoInventario entity) {
        return service.save(entity);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ALMACENISTA')")
    public ResponseEntity<MovimientoInventario> update(@PathVariable Integer id,
            @RequestBody MovimientoInventario entity) {
        return service.findById(id).map(existing -> {
            entity.setIdMovimiento(id);
            return ResponseEntity.ok(service.save(entity));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
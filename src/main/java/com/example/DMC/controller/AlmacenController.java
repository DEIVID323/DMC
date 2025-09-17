package com.example.DMC.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.DMC.model.Almacen;
import com.example.DMC.service.AlmacenService;

import java.util.List;

@RestController
@RequestMapping("/api/almacenes")
public class AlmacenController {
    @Autowired
    private AlmacenService service;

    @GetMapping
    @PreAuthorize("hasRole('admin')") // Basado en permisos como 'almacenes_gestionar'
    public List<Almacen> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Almacen> getById(@PathVariable Integer id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('admin')")
    public Almacen create(@RequestBody Almacen entity) {
        return service.save(entity);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Almacen> update(@PathVariable Integer id, @RequestBody Almacen entity) {
        return service.findById(id).map(existing -> {
            entity.setId(id);
            return ResponseEntity.ok(service.save(entity));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

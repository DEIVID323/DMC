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

import com.example.DMC.model.MovimientoCaja;
import com.example.DMC.service.MovimientoCajaService;

@RestController
@RequestMapping("/api/movimientos-caja")
public class MovimientoCajaController {
    @Autowired
    private MovimientoCajaService service;

    @GetMapping
    
    public List<MovimientoCaja> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
   
    public ResponseEntity<MovimientoCaja> getById(@PathVariable Integer id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
   
    public MovimientoCaja create(@RequestBody MovimientoCaja entity) {
        return service.save(entity);
    }

    @PutMapping("/{id}")
 
    public ResponseEntity<MovimientoCaja> update(@PathVariable Integer id, @RequestBody MovimientoCaja entity) {
        return service.findById(id).map(existing -> {
            entity.setIdMovimientoCaja(id);
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
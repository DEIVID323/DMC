package com.example.DMC.controller;

import java.time.LocalDate;
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

import com.example.DMC.model.TipoCambio;
import com.example.DMC.model.TipoCambioId;
import com.example.DMC.service.TipoCambioService;


@RestController
@RequestMapping("/api/tipo-cambios")
public class TipoCambioController {
    @Autowired
    private TipoCambioService service;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<TipoCambio> getAll() {
        return service.findAll();
    }

    @GetMapping("/{fecha}/{idMoneda}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TipoCambio> getById(@PathVariable LocalDate fecha, @PathVariable Integer idMoneda) {
        TipoCambioId id = new TipoCambioId();
        id.setFecha(fecha);
        id.setIdMoneda(idMoneda);
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public TipoCambio create(@RequestBody TipoCambio entity) {
        return service.save(entity);
    }

    @PutMapping("/{fecha}/{idMoneda}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TipoCambio> update(@PathVariable LocalDate fecha, @PathVariable Integer idMoneda,
            @RequestBody TipoCambio entity) {
        TipoCambioId id = new TipoCambioId();
        id.setFecha(fecha);
        id.setIdMoneda(idMoneda);
        return service.findById(id).map(existing -> {
            entity.setFecha(fecha);
            entity.setIdMoneda(idMoneda);
            return ResponseEntity.ok(service.save(entity));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{fecha}/{idMoneda}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable LocalDate fecha, @PathVariable Integer idMoneda) {
        TipoCambioId id = new TipoCambioId();
        id.setFecha(fecha);
        id.setIdMoneda(idMoneda);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
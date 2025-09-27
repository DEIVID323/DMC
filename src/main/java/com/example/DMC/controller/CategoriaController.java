package com.example.DMC.controller;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.DMC.model.Categoria;
import com.example.DMC.service.CategoriaService;



import java.util.List;

@Controller

public class CategoriaController {
    @Autowired
    private CategoriaService service;

    @GetMapping("/categorias")
    public String getAllCategories(Model model) {
        model.addAttribute("categorias", service.findAll());
        return "categoria";
    }



/* 
    @GetMapping
    @PreAuthorize("hasAnyRole('admin', 'CAJERO')")
    public List<Categoria> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('admin', 'CAJERO')")
    public ResponseEntity<Categoria> getById(@PathVariable Integer id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('admin')")
    public Categoria create(@RequestBody Categoria entity) {
        return service.save(entity);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Categoria> update(@PathVariable Integer id, @RequestBody Categoria entity) {
        return service.findById(id).map(existing -> {
            entity.setIdCategoria(id);
            return ResponseEntity.ok(service.save(entity));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
 */
}
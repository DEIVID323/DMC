package com.example.DMC.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.DMC.model.Usuario;
import com.example.DMC.service.UsuarioService;





@Controller

public class UsuarioController {
    @Autowired
    private UsuarioService service;
    @GetMapping("/usuarios")
        public String showusername(Model model) { 
            model.addAttribute("usuarios", service.findAll());
            // Solo carga el HTML, los datos se llenan desde el frontend
            return "usuario";
        } 


/* 
    @GetMapping
    @PreAuthorize("hasRole('admin')")
    public List<Usuario> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Usuario> getById(@PathVariable Integer id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('admin')")
    public Usuario create(@RequestBody Usuario entity) {
        return service.save(entity);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Usuario> update(@PathVariable Integer id, @RequestBody Usuario entity) {
        return service.findById(id).map(existing -> {
            entity.setIdUsuario(id);
            return ResponseEntity.ok(service.save(entity));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    } */

}
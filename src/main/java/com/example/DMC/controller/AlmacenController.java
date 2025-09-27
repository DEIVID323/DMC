package com.example.DMC.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.DMC.model.Almacen;
import com.example.DMC.repository.AlmacenRepository;
import com.example.DMC.service.AlmacenService;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/almacenes")
public class AlmacenController {

    private final AlmacenRepository almacenRepository;

    public AlmacenController(AlmacenRepository almacenRepository) {
        this.almacenRepository = almacenRepository;
    }

    // Listar almacenes
    @GetMapping
    public String listarAlmacenes(Model model) {
        List<Almacen> almacenes = almacenRepository.findAll();
        model.addAttribute("almacenes", almacenes);
        model.addAttribute("almacen", new Almacen()); // objeto vacío para modal de crear
        return "Almacenes/almacen";
    }

    // Guardar o actualizar (unificado)
    @PostMapping("/guardar")
    public String guardarAlmacen(@ModelAttribute("almacen") Almacen almacen) {
        almacenRepository.save(almacen);
        // si id == null → crea
        // si id != null → actualiza
        return "redirect:/almacenes";
    }

    // Buscar por ID (para el modal de editar)
    @GetMapping("/editar/{id}")
    @ResponseBody
    public Almacen editarAlmacen(@PathVariable Integer id) {
        return almacenRepository.findById(id).orElse(null);
    }

    // Eliminar
    @GetMapping("/eliminar/{id}")
    public String eliminarAlmacen(@PathVariable Integer id) {
        almacenRepository.deleteById(id);
        return "redirect:/almacenes";
    }
}

/* 
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
    } */


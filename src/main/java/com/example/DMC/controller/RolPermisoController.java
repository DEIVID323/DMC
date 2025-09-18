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

import com.example.DMC.model.RolPermiso;
import com.example.DMC.model.RolPermisoId;
import com.example.DMC.service.RolPermisoService;

@RestController
@RequestMapping("/api/rol-permisos")
public class RolPermisoController {
    @Autowired
    private RolPermisoService service;

    @GetMapping
    @PreAuthorize("hasRole('admin')")
    public List<RolPermiso> getAll() {
        return service.findAll();
    }

    @GetMapping("/{idRol}/{idPermiso}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<RolPermiso> getById(@PathVariable Integer idRol, @PathVariable Integer idPermiso) {
        RolPermisoId id = new RolPermisoId();
        id.setIdRol(idRol);
        id.setIdPermiso(idPermiso);
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('admin')")
    public RolPermiso create(@RequestBody RolPermiso entity) {
        return service.save(entity);
    }

    @PutMapping("/{idRol}/{idPermiso}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<RolPermiso> update(@PathVariable Integer idRol, @PathVariable Integer idPermiso,
            @RequestBody RolPermiso entity) {
        RolPermisoId id = new RolPermisoId();
        id.setIdRol(idRol);
        id.setIdPermiso(idPermiso);
        return service.findById(id).map(existing -> {
            entity.setIdRol(idRol);
            entity.setIdPermiso(idPermiso);
            return ResponseEntity.ok(service.save(entity));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{idRol}/{idPermiso}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Void> delete(@PathVariable Integer idRol, @PathVariable Integer idPermiso) {
        RolPermisoId id = new RolPermisoId();
        id.setIdRol(idRol);
        id.setIdPermiso(idPermiso);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
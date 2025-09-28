package com.example.DMC.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.DMC.model.Almacen;
import com.example.DMC.repository.AlmacenRepository;

import java.util.List;

@Controller
@RequestMapping("/almacenes")
public class AlmacenController {

    @Autowired
    private AlmacenRepository almacenRepository;

    // Listar almacenes
    @GetMapping
    public String listarAlmacenes(Model model) {
        List<Almacen> almacenes = almacenRepository.findAll();
        model.addAttribute("almacenes", almacenes);
        model.addAttribute("newAlmacen", new Almacen()); // para el modal de crear

        // Configuración para el layout (IGUAL QUE CATEGORIAS)
        model.addAttribute("view", "Almacenes/almacen"); // Apunta al fragmento 'almacen.html'
        model.addAttribute("activePage", "almacenes");

        return "layout"; // Retorna el layout principal que cargará el fragmento "almacen"
    }

    // Guardar almacén (crear)
    @PostMapping("/guardar")
    public String guardarAlmacen(@ModelAttribute("newAlmacen") Almacen almacen,
            @RequestParam(value = "activo", defaultValue = "false") boolean activo,
            RedirectAttributes ra) {

        almacen.setActivo(activo);
        almacenRepository.save(almacen);
        ra.addFlashAttribute("mensaje", "Almacén creado correctamente.");
        return "redirect:/almacenes";
    }

    // Actualizar almacén
    @PostMapping("/actualizar")
    public String actualizarAlmacen(@RequestParam("id") Integer id,
            @RequestParam("nombre") String nombre,
            @RequestParam("ubicacion") String ubicacion,
            @RequestParam(value = "activo", defaultValue = "false") boolean activo,
            RedirectAttributes ra) {

        Almacen almacen = almacenRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Almacén no encontrado: " + id));

        almacen.setNombre(nombre);
        almacen.setUbicacion(ubicacion);
        almacen.setActivo(activo);

        almacenRepository.save(almacen);
        ra.addFlashAttribute("mensaje", "Almacén actualizado correctamente.");
        return "redirect:/almacenes";
    }

    // Eliminar almacén
    @GetMapping("/eliminar/{id}")
    public String eliminarAlmacen(@PathVariable Integer id, RedirectAttributes ra) {
        almacenRepository.deleteById(id);
        ra.addFlashAttribute("mensaje", "Almacén eliminado.");
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


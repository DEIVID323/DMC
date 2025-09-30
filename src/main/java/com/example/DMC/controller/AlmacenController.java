package com.example.DMC.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize; // <- IMPORTANTE
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.DMC.model.Almacen;
import com.example.DMC.repository.AlmacenRepository;

import java.util.List;

@Controller
@RequestMapping("/almacenes")
// ✅ Solo ADMIN para todos los métodos de este controller
@PreAuthorize("hasRole('ADMIN')")
public class AlmacenController {

    @Autowired
    private AlmacenRepository almacenRepository;

    @GetMapping
    public String listarAlmacenes(Model model) {
        List<Almacen> almacenes = almacenRepository.findAll();
        model.addAttribute("almacenes", almacenes);
        model.addAttribute("newAlmacen", new Almacen());
        model.addAttribute("view", "Almacenes/almacen");
        model.addAttribute("activePage", "almacenes");
        return "layout";
    }

    @PostMapping("/guardar")
    public String guardarAlmacen(@ModelAttribute("newAlmacen") Almacen almacen,
            @RequestParam(value = "activo", defaultValue = "false") boolean activo,
            RedirectAttributes ra) {
        almacen.setActivo(activo);
        almacenRepository.save(almacen);
        ra.addFlashAttribute("mensaje", "Almacén creado correctamente.");
        return "redirect:/almacenes";
    }

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

    @GetMapping("/eliminar/{id}")
    public String eliminarAlmacen(@PathVariable Integer id, RedirectAttributes ra) {
        almacenRepository.deleteById(id);
        ra.addFlashAttribute("mensaje", "Almacén eliminado.");
        return "redirect:/almacenes";
    }
}

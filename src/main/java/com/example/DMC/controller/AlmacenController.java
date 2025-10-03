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
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class AlmacenController {

    @Autowired
    private AlmacenRepository almacenRepository;

    @GetMapping
    public String listarAlmacenes(Model model) {
        List<Almacen> almacenes = almacenRepository.findByActivoTrue();
        model.addAttribute("almacenes", almacenes);
        model.addAttribute("newAlmacen", new Almacen());
        model.addAttribute("mostrarInactivos", false);
        model.addAttribute("view", "Almacenes/almacen");
        model.addAttribute("activePage", "almacenes");
        return "layout";
    }

    @GetMapping("/inactivos")
    public String listarAlmacenesInactivos(Model model) {
        List<Almacen> almacenes = almacenRepository.findByActivoFalse();
        model.addAttribute("almacenes", almacenes);
        model.addAttribute("newAlmacen", new Almacen());
        model.addAttribute("mostrarInactivos", true);
        model.addAttribute("view", "Almacenes/almacen");
        model.addAttribute("activePage", "almacenes");
        return "layout";
    }

    @PostMapping("/guardar")
    public String guardarAlmacen(@ModelAttribute("newAlmacen") Almacen almacen,
            @RequestParam(value = "activo", defaultValue = "false") boolean activo,
            RedirectAttributes ra) {
        try {
            System.out.println("========================================");
            System.out.println("Guardar Almacén - Datos recibidos:");
            System.out.println("Nombre: " + almacen.getNombre());
            System.out.println("Ubicación: " + almacen.getUbicacion());
            System.out.println("Activo: " + activo);
            System.out.println("========================================");

            almacen.setActivo(activo);
            Almacen guardado = almacenRepository.save(almacen);

            System.out.println("Almacén guardado exitosamente con ID: " + guardado.getId());
            ra.addFlashAttribute("mensaje", "Almacén creado correctamente.");
        } catch (Exception e) {
            System.err.println("ERROR al guardar almacén: " + e.getMessage());
            e.printStackTrace();
            ra.addFlashAttribute("error", "Error al crear almacén: " + e.getMessage());
        }
        return "redirect:/almacenes";
    }

    @PostMapping("/actualizar")
    public String actualizarAlmacen(@RequestParam("id") Integer id,
            @RequestParam("nombre") String nombre,
            @RequestParam("ubicacion") String ubicacion,
            @RequestParam(value = "activo", defaultValue = "false") boolean activo,
            RedirectAttributes ra) {
        try {
            System.out.println("========================================");
            System.out.println("Actualizar Almacén - ID: " + id);
            System.out.println("Nombre: " + nombre);
            System.out.println("Ubicación: " + ubicacion);
            System.out.println("Activo: " + activo);
            System.out.println("========================================");

            Almacen almacen = almacenRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Almacén no encontrado: " + id));
            almacen.setNombre(nombre);
            almacen.setUbicacion(ubicacion);
            almacen.setActivo(activo);
            almacenRepository.save(almacen);

            System.out.println("Almacén actualizado exitosamente");
            ra.addFlashAttribute("mensaje", "Almacén actualizado correctamente.");
        } catch (Exception e) {
            System.err.println("ERROR al actualizar almacén: " + e.getMessage());
            e.printStackTrace();
            ra.addFlashAttribute("error", "Error al actualizar almacén: " + e.getMessage());
        }
        return "redirect:/almacenes";
    }

    @GetMapping("/inactivar/{id}")
    public String inactivarAlmacen(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            Almacen almacen = almacenRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Almacén no encontrado: " + id));
            almacen.setActivo(false);
            almacenRepository.save(almacen);
            ra.addFlashAttribute("mensaje", "Almacén inactivado correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error al inactivar almacén: " + e.getMessage());
        }
        return "redirect:/almacenes";
    }

    @GetMapping("/activar/{id}")
    public String activarAlmacen(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            Almacen almacen = almacenRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Almacén no encontrado: " + id));
            almacen.setActivo(true);
            almacenRepository.save(almacen);
            ra.addFlashAttribute("mensaje", "Almacén activado correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error al activar almacén: " + e.getMessage());
        }
        return "redirect:/almacenes/inactivos";
    }
}

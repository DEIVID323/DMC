package com.example.DMC.controller;

import com.example.DMC.model.Rol;
import com.example.DMC.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class RolController {

    private final RolRepository rolRepository;

    @GetMapping("/roles")
    public String listarRoles(Model model) {
        model.addAttribute("roles", rolRepository.findAll());
        model.addAttribute("view", "roles");
        model.addAttribute("activePage", "roles");
        return "layout";
    }

    // Crear
    // @PreAuthorize("hasAuthority('Administrador')")
    @PostMapping("/roles")
    public String crearRol(@RequestParam("nombreRol") String nombreRol,
            RedirectAttributes ra) {
        String nombre = (nombreRol == null) ? "" : nombreRol.trim();

        if (nombre.isEmpty()) {
            ra.addFlashAttribute("error", "El nombre del rol es obligatorio.");
            return "redirect:/roles";
        }
        if (nombre.length() > 50) {
            ra.addFlashAttribute("error", "El nombre del rol no puede superar 50 caracteres.");
            return "redirect:/roles";
        }
        if (rolRepository.existsByNombreRolIgnoreCase(nombre)) {
            ra.addFlashAttribute("error", "Ya existe un rol con ese nombre.");
            return "redirect:/roles";
        }

        Rol r = new Rol();
        r.setNombreRol(nombre);
        try {
            rolRepository.save(r);
            ra.addFlashAttribute("ok", "Rol creado correctamente.");
        } catch (DataIntegrityViolationException ex) {
            ra.addFlashAttribute("error", "No se pudo crear el rol (violación de restricción única).");
        }
        return "redirect:/roles";
    }

    // Actualizar
    // @PreAuthorize("hasAuthority('Administrador')")
    @PostMapping("/roles/{id}")
    public String actualizarRol(@PathVariable("id") Integer idRol,
            @RequestParam("nombreRol") String nombreRol,
            RedirectAttributes ra) {
        Rol rol = rolRepository.findById(idRol).orElse(null);
        if (rol == null) {
            ra.addFlashAttribute("error", "Rol no encontrado.");
            return "redirect:/roles";
        }

        String nombre = (nombreRol == null) ? "" : nombreRol.trim();
        if (nombre.isEmpty()) {
            ra.addFlashAttribute("error", "El nombre del rol es obligatorio.");
            return "redirect:/roles";
        }
        if (nombre.length() > 50) {
            ra.addFlashAttribute("error", "El nombre del rol no puede superar 50 caracteres.");
            return "redirect:/roles";
        }
        // Si cambia el nombre, verifica duplicado
        if (!nombre.equalsIgnoreCase(rol.getNombreRol())
                && rolRepository.existsByNombreRolIgnoreCase(nombre)) {
            ra.addFlashAttribute("error", "Ya existe un rol con ese nombre.");
            return "redirect:/roles";
        }

        rol.setNombreRol(nombre);
        try {
            rolRepository.save(rol);
            ra.addFlashAttribute("ok", "Rol actualizado.");
        } catch (DataIntegrityViolationException ex) {
            ra.addFlashAttribute("error", "No se pudo actualizar (violación de restricción única).");
        }
        return "redirect:/roles";
    }

    // Eliminar
    // @PreAuthorize("hasAuthority('Administrador')")
    @PostMapping("/roles/{id}/delete")
    public String eliminarRol(@PathVariable("id") Integer idRol,
            RedirectAttributes ra) {
        Rol rol = rolRepository.findById(idRol).orElse(null);
        if (rol == null) {
            ra.addFlashAttribute("error", "Rol no encontrado.");
            return "redirect:/roles";
        }
        try {
            rolRepository.delete(rol);
            ra.addFlashAttribute("ok", "Rol eliminado.");
        } catch (DataIntegrityViolationException ex) {
            ra.addFlashAttribute("error", "No se puede eliminar: el rol está en uso por usuarios.");
        }
        return "redirect:/roles";
    }
}

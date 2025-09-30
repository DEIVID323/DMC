package com.example.DMC.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.DMC.model.Usuario;
import com.example.DMC.model.Rol;
import com.example.DMC.service.UsuarioService;
import com.example.DMC.service.RolService;

@Controller
@RequestMapping("/usuarios")

public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolService rolService;

    // Listar usuarios
    @GetMapping
    public String listarUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.findAll();
        List<Rol> roles = rolService.findAll();

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("roles", roles);
        model.addAttribute("newUsuario", new Usuario()); // para el modal de crear

        // Configuración para el layout
        model.addAttribute("view", "usuario");
        model.addAttribute("activePage", "usuarios");

        return "layout";
    }

    // Guardar usuario (crear)
    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute("newUsuario") Usuario usuario,
            @RequestParam("idRol") Integer idRol,
            @RequestParam(value = "activo", defaultValue = "false") boolean activo,
            RedirectAttributes ra) {

        try {
            // Buscar el rol
            Rol rol = rolService.findById(idRol)
                    .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado: " + idRol));

            usuario.setRol(rol);
            usuario.setActivo(activo);

            usuarioService.save(usuario);
            ra.addFlashAttribute("mensaje", "Usuario creado correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error al crear usuario: " + e.getMessage());
        }

        return "redirect:/usuarios";
    }

    // Actualizar usuario
    @PostMapping("/actualizar")
    public String actualizarUsuario(@RequestParam("idUsuario") Integer idUsuario,
            @RequestParam("nombreCompleto") String nombreCompleto,
            @RequestParam("username") String username,
            @RequestParam("idRol") Integer idRol,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "activo", defaultValue = "false") boolean activo,
            RedirectAttributes ra) {

        try {
            Usuario usuario = usuarioService.findById(idUsuario)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + idUsuario));

            // Buscar el rol
            Rol rol = rolService.findById(idRol)
                    .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado: " + idRol));

            usuario.setNombreCompleto(nombreCompleto);
            usuario.setUsername(username);
            usuario.setRol(rol);
            usuario.setActivo(activo);

            // Solo actualizar contraseña si se proporciona
            if (password != null && !password.trim().isEmpty()) {
                usuario.setPassword(password);
            }

            usuarioService.save(usuario);
            ra.addFlashAttribute("mensaje", "Usuario actualizado correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error al actualizar usuario: " + e.getMessage());
        }

        return "redirect:/usuarios";
    }

    // Cambiar estado activo/inactivo
    @PostMapping("/toggleActivo")
    public String toggleActivo(@RequestParam("idUsuario") Integer idUsuario,
            @RequestParam("activo") boolean activo,
            RedirectAttributes ra) {

        try {
            Usuario usuario = usuarioService.findById(idUsuario)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + idUsuario));

            usuario.setActivo(activo);
            usuarioService.save(usuario);

            String mensaje = activo ? "Usuario activado correctamente." : "Usuario desactivado correctamente.";
            ra.addFlashAttribute("mensaje", mensaje);
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error al cambiar estado: " + e.getMessage());
        }

        return "redirect:/usuarios";
    }

    // Eliminar usuario (opcional)
    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            usuarioService.deleteById(id);
            ra.addFlashAttribute("mensaje", "Usuario eliminado correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error al eliminar usuario: " + e.getMessage());
        }
        return "redirect:/usuarios";
    }
}
package com.example.DMC.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.DMC.model.Usuario;
import com.example.DMC.model.Rol;
import com.example.DMC.repository.UsuarioRepository;
import com.example.DMC.repository.RolRepository;
import com.example.DMC.service.UsuarioService;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    // ✅ Listar usuarios + roles
    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        List<Usuario> usuarios = service.findAll();
        List<Rol> roles = rolRepository.findAll();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("roles", roles);
        return "usuario"; // Nombre del archivo usuario.html
    }

    // ✅ Guardar nuevo usuario
    @PostMapping("/usuarios/guardar")
    public String guardarUsuario(@ModelAttribute Usuario usuario, @RequestParam("idRol") Integer idRol) {
        Rol rol = rolRepository.findById(idRol)
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));
        usuario.setRol(rol);
        usuarioRepository.save(usuario);
        return "redirect:/usuarios";
    }

    // ✅ Actualizar usuario
    @PostMapping("/usuarios/actualizar")
    public String actualizarUsuario(@ModelAttribute Usuario usuario, @RequestParam("idRol") Integer idRol) {
        Rol rol = rolRepository.findById(idRol)
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));
        usuario.setRol(rol);

        // Si no se pasa password, mantener el anterior
        Usuario existente = usuarioRepository.findById(usuario.getIdUsuario())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        if (usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
            usuario.setPassword(existente.getPassword());
        }

        usuarioRepository.save(usuario);
        return "redirect:/usuarios";
    }

    // ✅ Activar/Inactivar usuario
    @PostMapping("/usuarios/toggleActivo")
    public String toggleActivo(@RequestParam("idUsuario") Integer idUsuario,
            @RequestParam("activo") boolean activo) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        usuario.setActivo(activo);
        usuarioRepository.save(usuario);
        return "redirect:/usuarios";
    }
}

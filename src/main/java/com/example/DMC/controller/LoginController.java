package com.example.DMC.controller;

import com.example.DMC.model.Rol;
import com.example.DMC.model.Usuario;
import com.example.DMC.repository.RolRepository;
import com.example.DMC.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UsuarioRepository usuarioRepo;
    private final RolRepository rolRepo;
    private final PasswordEncoder encoder;

    // Pantalla de login/registro
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * Registro de usuarios (solo nombreCompleto, username, password).
     * - Valida vacíos y longitudes
     * - Verifica duplicados de username
     * - Asigna rol por defecto (id = 2 -> ROLE_USER, por ejemplo)
     */
    @PostMapping("/register")
    public String register(@RequestParam String nombreCompleto,
            @RequestParam String username,
            @RequestParam String password,
            RedirectAttributes ra) {

        // Validaciones simples (puedes migrar a DTO + @Valid si gustas)
        if (isBlank(nombreCompleto) || isBlank(username) || isBlank(password)) {
            ra.addFlashAttribute("registroError", "Todos los campos son obligatorios.");
            return "redirect:/login?reg_error";
        }
        if (username.length() > 50) {
            ra.addFlashAttribute("registroError", "El usuario no puede superar 50 caracteres.");
            return "redirect:/login?reg_error";
        }
        if (password.length() < 6) {
            ra.addFlashAttribute("registroError", "La contraseña debe tener al menos 6 caracteres.");
            return "redirect:/login?reg_error";
        }
        if (usuarioRepo.existsByUsername(username.trim())) {
            ra.addFlashAttribute("registroError", "El nombre de usuario ya existe.");
            return "redirect:/login?reg_error";
        }

        // Rol por defecto: 2 (ajústalo a tu catálogo)
        Rol rol = rolRepo.findById(4)
                .orElse(null);
        if (rol == null) {
            ra.addFlashAttribute("registroError", "No se encontró el rol por defecto (id=2).");
            return "redirect:/login?reg_error";
        }

        // Construir y guardar el usuario
        Usuario u = new Usuario();
        u.setNombreCompleto(nombreCompleto.trim());
        u.setUsername(username.trim());
        u.setPassword(encoder.encode(password)); // BCrypt
        u.setRol(rol);
        // Los demás campos ya tienen valores por defecto en la entidad:
        // moneda="$", activo=true, fechaCreacion=now

        usuarioRepo.save(u);

        ra.addFlashAttribute("registroOk", "Cuenta creada. Inicia sesión.");
        return "redirect:/login?registered";
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}

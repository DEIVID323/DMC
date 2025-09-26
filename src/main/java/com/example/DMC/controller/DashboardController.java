package com.example.DMC.controller;


import com.example.DMC.model.DashboardData;
import com.example.DMC.service.DashboardService;
import com.example.DMC.service.UsuarioService;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

    public class DashboardController {
        @Autowired
    private UsuarioService service;

        @GetMapping("/dashboard")
        public String showDashboard() {
            // Solo carga el HTML, los datos se llenan desde el frontend
            return "dashboard";
        }
       
     @GetMapping("/hola")
    public String usuario(Model model) {
        String username = "Invitado";
        model.addAttribute("mensaje", username);
        return "index";
        
    }
 
}

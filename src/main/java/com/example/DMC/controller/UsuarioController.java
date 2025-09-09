package com.example.DMC.controller;

import com.example.DMC.entities.Usuario;
import com.example.DMC.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping ("/api")

public class UsuarioController {
    @Autowired
    private  UsuarioRepository userepo;

    @GetMapping
    public List<Usuario>usuarioList(){
        return  userepo.findAll();

    }
}

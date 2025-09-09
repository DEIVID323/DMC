package com.example.DMC.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.example.DMC.entities.Rol;

import com.example.DMC.repository.RolRepository;

@RestController
@RequestMapping ("/rol")
public class RolController {
     @Autowired
    private  RolRepository userepo;

    @GetMapping
    public List<Rol>rolList(){
        return  userepo.findAll();

    }
    
}

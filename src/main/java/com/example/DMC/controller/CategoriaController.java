package com.example.DMC.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.DMC.repository.CategoriaRepository;


@RestController
@RequestMapping ("/categoria")
public class CategoriaController {
     @Autowired
    private  CategoriaRepository userepo;

    @GetMapping
    public Object categoriaList(){
        return  userepo.findAll();

    }
    
}

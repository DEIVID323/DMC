package com.example.DMC.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.DMC.entities.Producto;

import com.example.DMC.repository.ProductoRepository;



@RestController
@RequestMapping ("/producto")

public class ProductoController {
    @Autowired
    private  ProductoRepository userepo;

    @GetMapping
    public List<Producto>productosList(){
        return  userepo.findAll();

    }
    
}

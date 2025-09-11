package com.example.DMC.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.DMC.entities.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
    
}

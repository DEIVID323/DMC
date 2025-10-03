package com.example.DMC.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.DMC.model.Categoria;
import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    List<Categoria> findByActivoTrue();
    List<Categoria> findByActivoFalse();
}

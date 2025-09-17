package com.example.DMC.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.DMC.model.Usuario;


public interface UsuarioRepository extends JpaRepository<Usuario, Integer> { 
    Usuario findByUsername(String username);
}
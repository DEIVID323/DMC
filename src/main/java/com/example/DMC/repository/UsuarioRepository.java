package com.example.DMC.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.DMC.model.Usuario;


public interface UsuarioRepository extends JpaRepository<Usuario, Integer> { 
    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByUsernameAndActivoTrue(String username);
    
    

    boolean existsByUsername(String username);


}
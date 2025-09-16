package com.example.DMC.service;






import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.example.DMC.model.Usuario;
import com.example.DMC.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository repository;

    public List<Usuario> findAll() {
        return repository.findAll();
    }

    public Optional<Usuario> findById(Integer id) {
        return repository.findById(id);
    }

    public Usuario save(Usuario entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
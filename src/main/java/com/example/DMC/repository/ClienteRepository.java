package com.example.DMC.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.DMC.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
}

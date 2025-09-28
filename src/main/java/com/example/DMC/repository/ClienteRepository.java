package com.example.DMC.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.DMC.model.Cliente;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    // Método para encontrar clientes activos
    List<Cliente> findByActivoTrue();

    // Método para encontrar clientes inactivos
    List<Cliente> findByActivoFalse();

    // Opcionalmente, puedes agregar más métodos útiles:

    // Buscar por documento de identidad
    Cliente findByDocumentoIdentidad(String documentoIdentidad);

    // Buscar por email
    Cliente findByEmail(String email);

    // Buscar por nombre (contiene)
    List<Cliente> findByNombreClienteContainingIgnoreCase(String nombre);
}
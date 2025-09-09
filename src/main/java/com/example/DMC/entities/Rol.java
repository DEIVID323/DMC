package com.example.DMC.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "rol", schema = "dmc")
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Long idRol;

    @Column(name = "nombre_rol")
    private String nombreRol;
}

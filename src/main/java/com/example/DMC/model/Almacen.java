package com.example.DMC.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "almacenes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Almacen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "ubicacion", length = 255)
    private String ubicacion;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
}

package com.example.DMC.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tipo_moneda")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoMoneda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "descripcion", nullable = false, length = 50)
    private String descripcion;

    @Column(name = "simbolo", nullable = false, length = 5)
    private String simbolo;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
}

package com.example.DMC.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rol_permiso")
@IdClass(RolPermisoId.class) // Necesitas una clase ID compuesta
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolPermiso {
    @Id
    @Column(name = "id_rol")
    private Integer idRol;

    @Id
    @Column(name = "id_permiso")
    private Integer idPermiso;

    @ManyToOne
    @JoinColumn(name = "id_rol", insertable = false, updatable = false)
    private Rol rol;

    @ManyToOne
    @JoinColumn(name = "id_permiso", insertable = false, updatable = false)
    private Permiso permiso;
}

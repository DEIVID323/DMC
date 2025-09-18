package com.example.DMC.model;

//Clase ID para RolPermiso

import java.io.Serializable;

import lombok.Data;
@Data
public class RolPermisoId implements Serializable {
    private Integer idRol;
    private Integer idPermiso;

    // getters, setters, equals, hashCode
}

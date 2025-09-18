package com.example.DMC.model;

// Clase ID para TipoCambio
import java.io.Serializable;
import java.time.LocalDate;

import lombok.Data;
@Data
public class TipoCambioId implements Serializable {
    private LocalDate fecha;
    private Integer idMoneda;

    // getters, setters, equals, hashCode
}
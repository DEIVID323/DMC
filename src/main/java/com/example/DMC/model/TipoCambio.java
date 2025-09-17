package com.example.DMC.model;

import java.math.BigDecimal;
import java.time.LocalDate;

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
@Table(name = "tipo_cambio")
@IdClass(TipoCambioId.class) // Clase ID compuesta
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoCambio {
    @Id
    @Column(name = "fecha")
    private LocalDate fecha;

    @Id
    @Column(name = "id_moneda")
    private Integer idMoneda;

    @Column(name = "tipo_moneda", nullable = false, length = 3)
    private String tipoMoneda;

    @Column(name = "valor_soles", nullable = false, precision = 10, scale = 4)
    private BigDecimal valorSoles;

    @ManyToOne
    @JoinColumn(name = "id_moneda", insertable = false, updatable = false)
    private TipoMoneda moneda;
}
package com.example.DMC.entities;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data

@Table(name = "producto", schema = "dmc")
public class Producto {
    @Id
    @GeneratedValue( strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "id_producto")

    private Long idProducto;
    private String nombre;
    private String referencia;
    private Double precio;
    private String descripcion;
    private int stock;
    private int precio_detal;
    private int precio_mayorista;

    private int categorias_id_categorias;

   
    
    

    
    


	
}

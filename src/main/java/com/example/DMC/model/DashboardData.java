package com.example.DMC.model;



import java.util.List;

public class DashboardData {

    private Usuario usuario; // Información del usuario autenticado
    private Long totalVentas; // Total de ventas completadas
    private Long totalCompras; // Total de compras recibidas
    private Long totalProductos; // Total de productos activos
    private Long totalProductosBajos; // Total de productos con stock bajo
    private List<String> labelsVentas; // Etiquetas para el gráfico (fechas)
    private List<Integer> dataVentas; // Datos para el gráfico (número de ventas)

    // Constructores
    public DashboardData() {
        // Constructor por defecto
    }

    public DashboardData(Usuario usuario, Long totalVentas, Long totalCompras, Long totalProductos,
            Long totalProductosBajos, List<String> labelsVentas, List<Integer> dataVentas) {
        this.usuario = usuario;
        this.totalVentas = totalVentas;
        this.totalCompras = totalCompras;
        this.totalProductos = totalProductos;
        this.totalProductosBajos = totalProductosBajos;
        this.labelsVentas = labelsVentas;
        this.dataVentas = dataVentas;
    }

    // Getters y Setters
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Long getTotalVentas() {
        return totalVentas;
    }

    public void setTotalVentas(Long totalVentas) {
        this.totalVentas = totalVentas;
    }

    public Long getTotalCompras() {
        return totalCompras;
    }

    public void setTotalCompras(Long totalCompras) {
        this.totalCompras = totalCompras;
    }

    public Long getTotalProductos() {
        return totalProductos;
    }

    public void setTotalProductos(Long totalProductos) {
        this.totalProductos = totalProductos;
    }

    public Long getTotalProductosBajos() {
        return totalProductosBajos;
    }

    public void setTotalProductosBajos(Long totalProductosBajos) {
        this.totalProductosBajos = totalProductosBajos;
    }

    public List<String> getLabelsVentas() {
        return labelsVentas;
    }

    public void setLabelsVentas(List<String> labelsVentas) {
        this.labelsVentas = labelsVentas;
    }

    public List<Integer> getDataVentas() {
        return dataVentas;
    }

    public void setDataVentas(List<Integer> dataVentas) {
        this.dataVentas = dataVentas;
    }
}
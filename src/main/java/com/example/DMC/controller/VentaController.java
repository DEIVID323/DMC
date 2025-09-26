package com.example.DMC.controller;


import com.example.DMC.model.Venta;
import com.example.DMC.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas") // 👈 prefijo para los endpoints
public class VentaController {

    
    @Autowired
    private VentaService ventaService;

    // Obtener todas las ventas
    @GetMapping
    public List<Venta> getAllVentas() {
        return ventaService.findAll();
    }

    // Obtener una venta por ID
    @GetMapping("/{id}")
    public Venta getVentaById(@PathVariable Integer id) {
        return ventaService.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + id));
    }

    // Crear una nueva venta
    @PostMapping
    public Venta createVenta(@RequestBody Venta venta) {
        return ventaService.save(venta);
    }

    // Actualizar una venta existente
    @PutMapping("/{id}")
    public Venta updateVenta(@PathVariable Integer id, @RequestBody Venta venta) {
        return ventaService.findById(id)
                .map(v -> {
                    v.setIdCliente(venta.getIdCliente());
                    v.setIdUsuario(venta.getIdUsuario());
                    v.setFechaVenta(venta.getFechaVenta());
                    v.setTotalVenta(venta.getTotalVenta());
                    v.setMetodoPago(venta.getMetodoPago());
                    v.setEstado(venta.getEstado());
                    return ventaService.save(v);
                })
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + id));
    }

    // Eliminar una venta
    @DeleteMapping("/{id}")
    public String deleteVenta(@PathVariable Integer id) {
        ventaService.deleteById(id);
        return "Venta con ID " + id + " eliminada correctamente";
    }
}

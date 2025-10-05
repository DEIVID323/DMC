package com.example.DMC.controller;

import com.example.DMC.model.Cliente;
import com.example.DMC.model.Producto;
import com.example.DMC.model.Venta;
import com.example.DMC.service.ClienteService;
import com.example.DMC.service.ProductoService;
import com.example.DMC.service.VentaService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/ventas")
@PreAuthorize("hasAnyRole('ADMINISTRADOR','CAJERO')")
@RequiredArgsConstructor
public class VentaController {

    private final VentaService ventaService;
    private final ClienteService clienteService;
    private final ProductoService productoService;

    // Vista POS (usa layout.html con fragment "Ventas/venta")
    @GetMapping
    public String mostrarPOS(Model model) {
        List<Cliente> clientes = clienteService.findByActivoTrue();
        System.out.println("========================================");
        System.out.println("Clientes activos encontrados: " + clientes.size());
        clientes.forEach(c -> System.out.println("  - " + c.getNombreCliente() + " (ID: " + c.getIdCliente() + ")"));
        System.out.println("========================================");
        model.addAttribute("clientes", clientes);
        model.addAttribute("view", "Ventas/venta");
        model.addAttribute("activePage", "ventas");
        return "layout";
    }

    // Vista Historial (tabla)
    @GetMapping("/historial")
    public String mostrarHistorial(Model model) {
        try {
            List<Venta> ventas = ventaService.findAllOrderByFechaDesc();
            model.addAttribute("ventas", ventas);
        } catch (Exception e) {
            model.addAttribute("ventas", Collections.emptyList());
            model.addAttribute("error", "Error al cargar el historial: " + e.getMessage());
        }
        model.addAttribute("view", "Ventas/ventahistorial");
        model.addAttribute("activePage", "ventas");
        return "layout";
    }

    // ==== APIs JSON (se quedan en este Controller con @ResponseBody) ====

    // Buscar productos (GET JSON)
    @GetMapping(value = "/buscar", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Producto> buscarProductos(@RequestParam("term") String term) {
        return productoService.buscarPorNombreOCodigo(term);
    }

    // Guardar venta (POST JSON)
    @PostMapping("/guardar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> guardarVenta(@RequestBody VentaController.VentaRequest req) {
        Map<String, Object> out = new HashMap<>();
        try {
            if (req == null || req.getCarrito() == null || req.getCarrito().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "error", "Carrito vacío"));
            }
            if (req.getMetodo_pago() == null || req.getMetodo_pago().isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "error", "Método de pago requerido"));
            }

            // DEBUG: verifica que SÍ llegas aquí
            System.out.println("[/ventas/guardar] req=" + req);

            Venta v = new Venta();
            if (req.getId_cliente() != null)
                v.setIdCliente(req.getId_cliente());
            v.setIdUsuario(1); /* ODO: del usuario autenticado */
            v.setFechaVenta(java.time.LocalDateTime.now());
            v.setMetodoPago(req.getMetodo_pago());
            v.setEstado(com.example.DMC.enums.EstadoVenta.COMPLETADA);

            double total = 0.0;
            for (VentaController.CarritoItem it : req.getCarrito()) {
                if (it.getId() == null || it.getCantidad() == null || it.getCantidad() <= 0) {
                    return ResponseEntity.badRequest().body(Map.of("success", false, "error", "Ítem inválido"));
                }
                var prod = productoService.findById(it.getId())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + it.getId()));
                total += prod.getPrecioVenta().doubleValue() * it.getCantidad();
                
            }
            v.setTotalVenta(total);

            var saved = ventaService.save(v);
            out.put("success", true);
            out.put("id_venta", saved.getIdVenta());
            return ResponseEntity.ok(out);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    // Ticket (vista independiente)
    @GetMapping("/ticket/{id}")
    public String mostrarTicket(@PathVariable Integer id, Model model) {
        Venta venta = ventaService.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        model.addAttribute("venta", venta);
        return "Ventas/ventaticket";
    }

    // === Manejador de excepciones del Controller (si algo explota, devuelve JSON
    // claro) ===
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleJsonParseError(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(400).body(Map.of(
                "success", false,
                "error", "Error al leer los datos JSON. Verifica el formato de los datos enviados."));
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleAny(Exception e) {
        e.printStackTrace();
        String errorMessage = e.getMessage() != null ? e.getMessage() : "Error interno del servidor";
        return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "error", errorMessage));
    }

    // ====== DTOs para recibir el JSON ======
    @Data
    public static class VentaRequest {
        private Integer id_cliente;
        private String metodo_pago;
        private List<CarritoItem> carrito;
    }

    @Data
    public static class CarritoItem {
        private Integer id;
        private Integer cantidad;
    }
}

package com.example.DMC.controller;

import com.example.DMC.model.Venta;
import com.example.DMC.model.Cliente;
import com.example.DMC.model.Producto;
import com.example.DMC.service.VentaService;
import com.example.DMC.service.ClienteService;
import com.example.DMC.service.ProductoService;
import com.example.DMC.enums.EstadoVenta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Controller
@RequestMapping("/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ProductoService productoService;

    // Mostrar POS (Punto de Venta)
    @GetMapping

    public String mostrarPOS(Model model) {
        List<Cliente> clientes = clienteService.findByActivoTrue();
        model.addAttribute("clientes", clientes);

        // Configuración para el layout
        model.addAttribute("view", "Ventas/venta");
        model.addAttribute("activePage", "ventas");

        return "layout";
    }

    // Mostrar historial de ventas
    @GetMapping("/historial")

    public String mostrarHistorial(Model model) {
        try {
            List<Venta> ventas = ventaService.findAllOrderByFechaDesc();

            // DEBUG: Verificar datos
            System.out.println("=== DEBUG VENTAS ===");
            System.out.println("Número de ventas encontradas: " + ventas.size());

            if (!ventas.isEmpty()) {
                Venta primeraVenta = ventas.get(0);
                System.out.println("Primera venta - ID: " + primeraVenta.getIdVenta());
                System.out.println("Primera venta - Total: " + primeraVenta.getTotalVenta());
                System.out.println("Primera venta - Estado: " + primeraVenta.getEstado());
                System.out.println("Primera venta - Fecha: " + primeraVenta.getFechaVenta());
                System.out.println("Primera venta - Método pago: " + primeraVenta.getMetodoPago());
            } else {
                System.out.println("No se encontraron ventas en la base de datos");
            }

            model.addAttribute("ventas", ventas);
            model.addAttribute("view", "Ventas/ventahistorial");
            model.addAttribute("activePage", "ventas");

            return "layout";

        } catch (Exception e) {
            System.err.println("ERROR en mostrarHistorial: " + e.getMessage());
            e.printStackTrace();

            // En caso de error, enviar lista vacía
            model.addAttribute("ventas", new ArrayList<>());
            model.addAttribute("error", "Error al cargar el historial: " + e.getMessage());
            model.addAttribute("view", "Ventas/ventahistorial");
            model.addAttribute("activePage", "ventas");
            return "layout";
        }
    }

    // API: Buscar productos para el POS
    @GetMapping("/buscar")
    @ResponseBody
 
    public ResponseEntity<List<Producto>> buscarProductos(@RequestParam("term") String term) {
        List<Producto> productos = productoService.buscarPorNombreOCodigo(term);
        return ResponseEntity.ok(productos);
    }

    // API: Guardar venta
    @PostMapping("/guardar")
    @ResponseBody

    public ResponseEntity<Map<String, Object>> guardarVenta(@RequestBody Map<String, Object> ventaData) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Crear la venta
            Venta venta = new Venta();

            // Cliente (opcional)
            String clienteId = (String) ventaData.get("id_cliente");
            if (clienteId != null && !clienteId.isEmpty()) {
                venta.setIdCliente(Integer.parseInt(clienteId));
            }

            // Usuario (temporalmente hardcodeado - deberías obtenerlo del contexto de
            // seguridad)
            venta.setIdUsuario(1); // TODO: Obtener del usuario logueado

            venta.setFechaVenta(LocalDateTime.now());
            venta.setMetodoPago((String) ventaData.get("metodo_pago"));
            venta.setEstado(EstadoVenta.COMPLETADA);

            // Calcular total y procesar carrito
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> carrito = (List<Map<String, Object>>) ventaData.get("carrito");

            double total = 0.0;
            for (Map<String, Object> item : carrito) {
                Integer productoId = (Integer) item.get("id");
                Integer cantidad = (Integer) item.get("cantidad");

                Producto producto = productoService.findById(productoId)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + productoId));

                total += producto.getPrecioVenta().doubleValue() * cantidad;

                // TODO: Aquí deberías guardar los detalles de venta y actualizar stock
            }

            venta.setTotalVenta(total);

            // Guardar venta
            Venta ventaGuardada = ventaService.save(venta);

            System.out.println("Venta guardada exitosamente - ID: " + ventaGuardada.getIdVenta());

            response.put("success", true);
            response.put("id_venta", ventaGuardada.getIdVenta());

        } catch (Exception e) {
            System.err.println("Error al guardar venta: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("error", e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    // Mostrar ticket de venta
    @GetMapping("/ticket/{id}")
    public String mostrarTicket(@PathVariable Integer id, Model model) {
        Venta venta = ventaService.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        model.addAttribute("venta", venta);

        // TODO: Agregar detalles de venta y información del cliente
        // model.addAttribute("detalles", detalleVentaService.findByVentaId(id));
        // model.addAttribute("cliente", clienteService.findById(venta.getIdCliente()));

        return "Ventas/ventaticket"; // Vista independiente para el ticket
    }

    
    }

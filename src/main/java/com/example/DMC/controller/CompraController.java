package com.example.DMC.controller;

import com.example.DMC.enums.EstadoCompra;
import com.example.DMC.model.Compra;
import com.example.DMC.service.AlmacenService;
import com.example.DMC.service.CompraService;
import com.example.DMC.service.ProveedorService;
import com.example.DMC.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/compras")
public class CompraController {

    @Autowired
    private CompraService compraService;

    @Autowired
    private ProveedorService proveedorService;

    @Autowired
    private AlmacenService almacenService;

    @Autowired
    private UsuarioService usuarioService;

    // Listar compras
    @GetMapping
    public String listarCompras(Model model) {
        List<Compra> compras = compraService.findAll();
        model.addAttribute("compras", compras);
        model.addAttribute("newCompra", new Compra());
        model.addAttribute("proveedores", proveedorService.findAll());
        model.addAttribute("almacenes", almacenService.findAll());

        // Configuración para el layout
        model.addAttribute("view", "compra");
        model.addAttribute("activePage", "compras");

        return "layout";
    }

    // Guardar compra (crear)
    @PostMapping("/guardar")
    public String guardarCompra(
            @RequestParam("idProveedor") Integer idProveedor,
            @RequestParam("idAlmacen") Integer idAlmacen,
            @RequestParam("totalCompra") BigDecimal totalCompra,
            Authentication authentication,
            RedirectAttributes ra) {

        try {
            // Obtener usuario actual o usar uno por defecto
            String username;
            if (authentication != null && authentication.getName() != null) {
                username = authentication.getName();
            } else {
                // Usuario por defecto si no hay autenticación
                username = "admin"; // O el username que tengas en tu BD
            }

            var usuario = usuarioService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            Compra compra = new Compra();
            compra.setProveedor(proveedorService.findById(idProveedor)
                    .orElseThrow(() -> new RuntimeException("Proveedor no encontrado")));
            compra.setAlmacen(almacenService.findById(idAlmacen)
                    .orElseThrow(() -> new RuntimeException("Almacén no encontrado")));
            compra.setUsuario(usuario);
            compra.setFechaCompra(LocalDateTime.now());
            compra.setTotalCompra(totalCompra);
            compra.setEstado(EstadoCompra.solicitada);

            compraService.save(compra);
            ra.addFlashAttribute("mensaje", "Orden de compra creada correctamente.");
            return "redirect:/compras";

        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute("error", "Error al crear la orden: " + e.getMessage());
            return "redirect:/compras";
        }
    }
    // Marcar compra como recibida
    @GetMapping("/recibir/{id}")
    public String recibirCompra(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            Compra compra = compraService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Compra no encontrada"));

            if (compra.getEstado() != EstadoCompra.solicitada) {
                ra.addFlashAttribute("error", "Solo se pueden recibir compras en estado 'solicitada'.");
                return "redirect:/compras";
            }

            compra.setEstado(EstadoCompra.recibida);
            compra.setFechaRecepcion(LocalDateTime.now());
            compraService.save(compra);

            ra.addFlashAttribute("mensaje", "Compra recibida correctamente.");
            return "redirect:/compras";

        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error al recibir la compra: " + e.getMessage());
            return "redirect:/compras";
        }
    }

    // Eliminar compra
    @GetMapping("/eliminar/{id}")
    public String eliminarCompra(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            Compra compra = compraService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Compra no encontrada"));

            if (compra.getEstado() == EstadoCompra.recibida) {
                ra.addFlashAttribute("error", "No se puede eliminar una compra ya recibida.");
                return "redirect:/compras";
            }

            compraService.deleteById(id);
            ra.addFlashAttribute("mensaje", "Compra eliminada correctamente.");
            return "redirect:/compras";

        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error al eliminar la compra: " + e.getMessage());
            return "redirect:/compras";
        }
    }
}
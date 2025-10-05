package com.example.DMC.controller;

import com.example.DMC.model.Almacen;
import com.example.DMC.model.MovimientoInventario;
import com.example.DMC.model.Producto;
import com.example.DMC.model.Usuario;
import com.example.DMC.service.AlmacenService;
import com.example.DMC.service.MovimientoInventarioService;
import com.example.DMC.service.ProductoService;
import com.example.DMC.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/movimientos-inventario")
public class MovimientoInventarioController {

    @Autowired
    private MovimientoInventarioService movimientoService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private AlmacenService almacenService;

    @Autowired
    private UsuarioService usuarioService;

    // Listar movimientos de inventario (vista única con modales)
    @GetMapping
    public String listarMovimientos(Model model) {
        List<MovimientoInventario> movimientos = movimientoService.findAllOrderByFechaDesc();
        List<Producto> productos = productoService.findByActivoTrue();
        List<Almacen> almacenes = almacenService.findByActivoTrue();

        model.addAttribute("movimientos", movimientos);
        model.addAttribute("productos", productos);
        model.addAttribute("almacenes", almacenes);

        // Configuración para el layout
        model.addAttribute("view", "movimientoinventario");
        model.addAttribute("activePage", "movimientos-inventario");

        return "layout";
    }

    // Registrar transferencia completa
    @PostMapping("/registrar")
    public String registrarTransferencia(@RequestParam("idProducto") Integer idProducto,
            @RequestParam("idAlmacenOrigen") Integer idAlmacenOrigen,
            @RequestParam("idAlmacenDestino") Integer idAlmacenDestino,
            @RequestParam("cantidad") Integer cantidad,
            RedirectAttributes ra) {

        try {
            // Validaciones básicas
            if (cantidad <= 0) {
                ra.addFlashAttribute("error", "La cantidad debe ser mayor a cero.");
                return "redirect:/movimientos-inventario";
            }

            if (idAlmacenOrigen.equals(idAlmacenDestino)) {
                ra.addFlashAttribute("error", "El almacén de origen debe ser diferente al de destino.");
                return "redirect:/movimientos-inventario";
            }

            // Obtener entidades
            Producto producto = productoService.findById(idProducto)
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + idProducto));

            Almacen almacenOrigen = almacenService.findById(idAlmacenOrigen)
                    .orElseThrow(
                            () -> new IllegalArgumentException("Almacén de origen no encontrado: " + idAlmacenOrigen));

            Almacen almacenDestino = almacenService.findById(idAlmacenDestino)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Almacén de destino no encontrado: " + idAlmacenDestino));

            Usuario usuarioActual = obtenerUsuarioActual();

            // Realizar transferencia completa (salida + entrada)
            movimientoService.realizarTransferencia(producto, almacenOrigen, almacenDestino, cantidad,
                    usuarioActual, "Transferencia entre almacenes");

            ra.addFlashAttribute("mensaje",
                    "Transferencia realizada correctamente. Se registraron los movimientos de salida y entrada.");

        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error al realizar la transferencia: " + e.getMessage());
        }

        return "redirect:/movimientos-inventario";
    }

    // Eliminar movimiento
    @GetMapping("/eliminar/{id}")
    public String eliminarMovimiento(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            // Obtener el movimiento antes de eliminarlo para revertir el stock si es
            // necesario
            MovimientoInventario movimiento = movimientoService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Movimiento no encontrado: " + id));

            // IMPORTANTE: Al eliminar un movimiento, deberías considerar revertir el cambio
            // en el stock
            // Por simplicidad, aquí solo eliminamos el registro
            // En un sistema real, tendrías que implementar lógica para revertir el stock

            movimientoService.deleteById(id);
            ra.addFlashAttribute("mensaje", "Movimiento eliminado correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error al eliminar el movimiento: " + e.getMessage());
        }
        return "redirect:/movimientos-inventario";
    }

    // Endpoint AJAX para obtener stock actual de producto (opcional)
    @GetMapping("/producto/{id}/stock")
    @ResponseBody
    public Integer obtenerStockProducto(@PathVariable Integer id) {
        return productoService.findById(id)
                .map(Producto::getStock)
                .orElse(0);
    }

    // Método auxiliar para obtener el usuario actual
    private Usuario obtenerUsuarioActual() {
        // Implementación temporal - esto debe adaptarse a tu sistema de seguridad
        // Por ejemplo, usando Spring Security:
        // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // String username = auth.getName();
        // return usuarioService.findByUsername(username);

        // Por ahora retornamos un usuario por defecto (ID 1)
        return usuarioService.findById(1)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }
}
package com.example.DMC.controller;

import com.example.DMC.model.Proveedor;
import com.example.DMC.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService service;

    // Listar proveedores activos
    @GetMapping
    public String listarProveedoresActivos(Model model) {
        List<Proveedor> proveedores = service.findByActivoTrue();
        model.addAttribute("proveedores", proveedores);
        model.addAttribute("newProveedor", new Proveedor()); // para el modal de crear
        model.addAttribute("viendoInactivos", false);

        // Configuración para el layout
        model.addAttribute("view", "proveedor");
        model.addAttribute("activePage", "proveedores");

        return "layout";
    }

    // Listar proveedores inactivos
    @GetMapping("/inactivos")
    public String listarProveedoresInactivos(Model model) {
        List<Proveedor> proveedores = service.findByActivoFalse();
        model.addAttribute("proveedores", proveedores);
        model.addAttribute("newProveedor", new Proveedor());
        model.addAttribute("viendoInactivos", true);

        // Configuración para el layout
        model.addAttribute("view", "proveedor");
        model.addAttribute("activePage", "proveedores");

        return "layout";
    }

    // Guardar proveedor (crear)
    @PostMapping("/guardar")
    public String guardarProveedor(@ModelAttribute("newProveedor") Proveedor proveedor,
            @RequestParam(value = "activo", defaultValue = "true") boolean activo,
            RedirectAttributes ra) {

        // Establecer activo por defecto si no se especifica
        proveedor.setActivo(activo);

        try {
            service.save(proveedor);
            ra.addFlashAttribute("mensaje", "Proveedor guardado correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error al guardar el proveedor: " + e.getMessage());
        }

        return "redirect:/proveedores";
    }

    // Actualizar proveedor
    @PostMapping("/actualizar")
    public String actualizarProveedor(@RequestParam("idProveedor") Integer idProveedor,
            @RequestParam("nombreProveedor") String nombreProveedor,
            @RequestParam(value = "ruc", required = false) String ruc,
            @RequestParam(value = "telefono", required = false) String telefono,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "direccion", required = false) String direccion,
            @RequestParam(value = "activo", defaultValue = "false") boolean activo,
            RedirectAttributes ra) {

        try {
            Proveedor proveedor = service.findById(idProveedor)
                    .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado: " + idProveedor));

            proveedor.setNombreProveedor(nombreProveedor);
            proveedor.setRuc(ruc);
            proveedor.setTelefono(telefono);
            proveedor.setEmail(email);
            proveedor.setDireccion(direccion);
            proveedor.setActivo(activo);

            service.save(proveedor);
            ra.addFlashAttribute("mensaje", "Proveedor actualizado correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error al actualizar el proveedor: " + e.getMessage());
        }

        return "redirect:/proveedores";
    }

    // Cambiar estado del proveedor (activar/desactivar)
    @GetMapping("/cambiarEstado/{id}/{estado}")
    public String cambiarEstado(@PathVariable Integer id,
            @PathVariable Integer estado,
            RedirectAttributes ra) {
        try {
            Proveedor proveedor = service.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado: " + id));

            boolean nuevoEstado = estado == 1;
            proveedor.setActivo(nuevoEstado);
            service.save(proveedor);

            String mensaje = nuevoEstado ? "Proveedor activado correctamente." : "Proveedor desactivado correctamente.";
            ra.addFlashAttribute("mensaje", mensaje);
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error al cambiar el estado: " + e.getMessage());
        }

        return "redirect:/proveedores";
    }

    // Eliminar proveedor
    @GetMapping("/eliminar/{id}")
    public String eliminarProveedor(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            service.deleteById(id);
            ra.addFlashAttribute("mensaje", "Proveedor eliminado correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error al eliminar el proveedor: " + e.getMessage());
        }
        return "redirect:/proveedores";
    }

    // Endpoint para obtener datos del proveedor (para el modal de edición - AJAX)
    @GetMapping("/datos/{id}")
    @ResponseBody
    public Proveedor obtenerDatosProveedor(@PathVariable Integer id) {
        return service.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado: " + id));
    }
}
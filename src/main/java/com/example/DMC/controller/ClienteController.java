package com.example.DMC.controller;

import com.example.DMC.model.Cliente;
import com.example.DMC.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService service;

    // Listar clientes activos
    @GetMapping
    public String listarClientes(Model model) {
        List<Cliente> clientes = service.findByActivoTrue(); // Solo activos
        model.addAttribute("clientes", clientes);
        model.addAttribute("newCliente", new Cliente()); // para el modal de crear
        model.addAttribute("mostrandoInactivos", false);

        // Configuración para el layout
        model.addAttribute("view", "cliente");
        model.addAttribute("activePage", "clientes");

        return "layout";
    }

    // Listar clientes inactivos
    @GetMapping("/inactivos")
    public String listarClientesInactivos(Model model) {
        List<Cliente> clientes = service.findByActivoFalse(); // Solo inactivos
        model.addAttribute("clientes", clientes);
        model.addAttribute("newCliente", new Cliente());
        model.addAttribute("mostrandoInactivos", true);

        // Configuración para el layout
        model.addAttribute("view", "cliente");
        model.addAttribute("activePage", "clientes");

        return "layout";
    }

    // Guardar cliente (crear)
    @PostMapping("/guardar")
    public String guardarCliente(@ModelAttribute("newCliente") Cliente cliente,
            RedirectAttributes ra) {

        // Establecer como activo por defecto si es nuevo
        if (cliente.getIdCliente() == null) {
            cliente.setActivo(true);
        }

        service.save(cliente);
        ra.addFlashAttribute("mensaje", "Cliente guardado correctamente.");
        return "redirect:/clientes";
    }

    // Actualizar cliente
    @PostMapping("/actualizar")
    public String actualizarCliente(@RequestParam("idCliente") Integer idCliente,
            @RequestParam("nombreCliente") String nombreCliente,
            @RequestParam("documentoIdentidad") String documentoIdentidad,
            @RequestParam("telefono") String telefono,
            @RequestParam("email") String email,
            @RequestParam("direccion") String direccion,
            RedirectAttributes ra) {

        Cliente cliente = service.findById(idCliente)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado: " + idCliente));

        cliente.setNombreCliente(nombreCliente);
        cliente.setDocumentoIdentidad(documentoIdentidad);
        cliente.setTelefono(telefono);
        cliente.setEmail(email);
        cliente.setDireccion(direccion);
        // No modificamos el estado activo en actualización

        service.save(cliente);
        ra.addFlashAttribute("mensaje", "Cliente actualizado correctamente.");
        return "redirect:/clientes";
    }

    // Cambiar estado (activar/desactivar)
    @GetMapping("/cambiarEstado/{id}/{estado}")
    public String cambiarEstado(@PathVariable Integer id, @PathVariable Integer estado,
            RedirectAttributes ra) {
        
        Cliente cliente = service.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado: " + id));

        boolean nuevoEstado = estado == 1;
        cliente.setActivo(nuevoEstado);
        service.save(cliente);

        String mensaje = nuevoEstado ? "Cliente activado correctamente." : "Cliente desactivado correctamente.";
        ra.addFlashAttribute("mensaje", mensaje);

        // Redirigir según el estado actual
        return nuevoEstado ? "redirect:/clientes" : "redirect:/clientes/inactivos";
    }

    // Eliminar permanentemente (opcional)
    @GetMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable Integer id, RedirectAttributes ra) {
        service.deleteById(id);
        ra.addFlashAttribute("mensaje", "Cliente eliminado permanentemente.");
        return "redirect:/clientes";
    }
}


/* @GetMapping
    @PreAuthorize("hasAnyRole('admin', 'CAJERO')")

    public List<Cliente> getAll()
    {

        return service.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('admin', 'CAJERO')")
    public ResponseEntity<Cliente> getById(@PathVariable Integer id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('admin', 'CAJERO')")
    public Cliente create(@RequestBody Cliente entity) {
        return service.save(entity);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('admin', 'CAJERO')")
    public ResponseEntity<Cliente> update(@PathVariable Integer id, @RequestBody Cliente entity) {
        return service.findById(id).map(existing -> {
            entity.setIdCliente(id);
            return ResponseEntity.ok(service.save(entity));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    } */


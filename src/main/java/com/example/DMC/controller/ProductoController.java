package com.example.DMC.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.DMC.model.Producto;
import com.example.DMC.service.AlmacenService;
import com.example.DMC.service.CategoriaService;
import com.example.DMC.service.ProductoService;
import com.example.DMC.service.ProveedorService;

@Controller
public class ProductoController {
    @Autowired
    private ProductoService service;
    @Autowired
    private CategoriaService categoriaService; // necesitas inyectar estos servicios
    @Autowired
    private AlmacenService almacenService;
    @Autowired
    
    private ProveedorService proveedorService;

    @GetMapping("/productos")
    public String getAllProductos(Model model) {
        model.addAttribute("productos", service.findAll());
        model.addAttribute("categorias", categoriaService.findAll()); // necesitas inyectar categoriaService
        model.addAttribute("almacenes", almacenService.findAll()); // idem
        model.addAttribute("proveedores", proveedorService.findAll()); // idem
        model.addAttribute("newProducto", new Producto());
        return "Productos/producto";
    }
@PostMapping("/guardar")
public String guardarProducto(@ModelAttribute Producto producto, RedirectAttributes redirectAttributes) {
    // El controller debería manejar la conversión de IDs a objetos
    service.save(producto);
    return "redirect:/productos";
}





/* 
    @GetMapping
    @PreAuthorize("hasAnyRole('admin', 'CAJERO', 'ALMACENISTA')")
    public List<Producto> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('admin', 'CAJERO', 'ALMACENISTA')")
    public ResponseEntity<Producto> getById(@PathVariable Integer id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('admin', 'ALMACENISTA')")
    public Producto create(@RequestBody Producto entity) {
        return service.save(entity);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('admin', 'ALMACENISTA')")
    public ResponseEntity<Producto> update(@PathVariable Integer id, @RequestBody Producto entity) {
        return service.findById(id).map(existing -> {
            entity.setIdProducto(id);
            return ResponseEntity.ok(service.save(entity));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    } */
}

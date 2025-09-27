package com.example.DMC.controller;

import com.example.DMC.model.Producto;
import com.example.DMC.model.Categoria;
import com.example.DMC.model.Almacen;
import com.example.DMC.model.Proveedor;
import com.example.DMC.repository.ProductoRepository;
import com.example.DMC.repository.CategoriaRepository;
import com.example.DMC.repository.AlmacenRepository;
import com.example.DMC.repository.ProveedorRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final AlmacenRepository almacenRepository;
    private final ProveedorRepository proveedorRepository;

    public ProductoController(ProductoRepository productoRepository,
            CategoriaRepository categoriaRepository,
            AlmacenRepository almacenRepository,
            ProveedorRepository proveedorRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.almacenRepository = almacenRepository;
        this.proveedorRepository = proveedorRepository;
    }

    // Listar productos
    @GetMapping
    public String listarProductos(Model model) {
        List<Producto> productos = productoRepository.findAll();
        model.addAttribute("productos", productos);
        model.addAttribute("newProducto", new Producto());

        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("almacenes", almacenRepository.findAll());
        model.addAttribute("proveedores", proveedorRepository.findAll());

        return "Productos/producto";
    }

    // Guardar producto (crear)
    @PostMapping("/guardar")
    public String guardarProducto(@RequestParam("idCategoria") Integer idCategoria,
            @RequestParam("idAlmacen") Integer idAlmacen,
            @RequestParam(value = "idProveedor", required = false) Integer idProveedor,
            @RequestParam(value = "activo", defaultValue = "false") boolean activo,
            @ModelAttribute Producto producto) {

        producto.setCategoria(categoriaRepository.findById(idCategoria).orElse(null));
        producto.setAlmacen(almacenRepository.findById(idAlmacen).orElse(null));
        if (idProveedor != null) {
            producto.setProveedorPreferido(proveedorRepository.findById(idProveedor).orElse(null));
        } else {
            producto.setProveedorPreferido(null);
        }

        producto.setActivo(activo);
        producto.setFechaCreacion(LocalDateTime.now());
        producto.setFechaActualizacion(LocalDateTime.now());

        productoRepository.save(producto);
        return "redirect:/productos";
    }

    // Actualizar producto (mejor: traer de BD y actualizar campos)
    @PostMapping("/actualizar")
    public String actualizarProducto(
            @RequestParam("idProducto") Integer idProducto,
            @RequestParam("idCategoria") Integer idCategoria,
            @RequestParam("idAlmacen") Integer idAlmacen,
            @RequestParam(value = "idProveedor", required = false) Integer idProveedor,
            @RequestParam(value = "activo", defaultValue = "false") boolean activo,
            @ModelAttribute Producto productoForm) {

        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + idProducto));

        // Actualizamos solo los campos editables
        producto.setCodigoBarra(productoForm.getCodigoBarra());
        producto.setNombre(productoForm.getNombre());
        producto.setDescripcion(productoForm.getDescripcion());
        producto.setPrecioCompra(productoForm.getPrecioCompra());
        producto.setPrecioVenta(productoForm.getPrecioVenta());
        producto.setStock(productoForm.getStock());
        producto.setStockMinimo(productoForm.getStockMinimo());

        producto.setCategoria(categoriaRepository.findById(idCategoria).orElse(null));
        producto.setAlmacen(almacenRepository.findById(idAlmacen).orElse(null));
        if (idProveedor != null) {
            producto.setProveedorPreferido(proveedorRepository.findById(idProveedor).orElse(null));
        } else {
            producto.setProveedorPreferido(null);
        }

        // Aquí se aplica el valor correcto enviado por el formulario
        producto.setActivo(activo);

        producto.setFechaActualizacion(LocalDateTime.now());

        productoRepository.save(producto);

        return "redirect:/productos";
    }

    // Eliminar producto
    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Integer id) {
        productoRepository.deleteById(id);
        return "redirect:/productos";
    }
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


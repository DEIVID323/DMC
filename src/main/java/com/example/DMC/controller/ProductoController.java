package com.example.DMC.controller;

import com.example.DMC.model.Producto;
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

    // Listar productos activos
    @GetMapping
    public String listarProductos(Model model) {
        List<Producto> productos = productoRepository.findByActivoTrue();
        model.addAttribute("productos", productos);
        model.addAttribute("newProducto", new Producto());
        model.addAttribute("mostrarInactivos", false);

        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("almacenes", almacenRepository.findAll());
        model.addAttribute("proveedores", proveedorRepository.findAll());

        model.addAttribute("view", "productos/producto");
        model.addAttribute("activePage", "productos");

        return "layout";
    }

    // Listar productos inactivos
    @GetMapping("/inactivos")
    public String listarProductosInactivos(Model model) {
        List<Producto> productos = productoRepository.findByActivoFalse();
        model.addAttribute("productos", productos);
        model.addAttribute("newProducto", new Producto());
        model.addAttribute("mostrarInactivos", true);

        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("almacenes", almacenRepository.findAll());
        model.addAttribute("proveedores", proveedorRepository.findAll());

        model.addAttribute("view", "productos/producto");
        model.addAttribute("activePage", "productos");

        return "layout";
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

    // Actualizar producto
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

        producto.setActivo(activo);
        producto.setFechaActualizacion(LocalDateTime.now());

        productoRepository.save(producto);

        return "redirect:/productos";
    }

    // Inactivar producto
    @GetMapping("/inactivar/{id}")
    public String inactivarProducto(@PathVariable Integer id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + id));
        producto.setActivo(false);
        producto.setFechaActualizacion(LocalDateTime.now());
        productoRepository.save(producto);
        return "redirect:/productos";
    }

    // Activar producto
    @GetMapping("/activar/{id}")
    public String activarProducto(@PathVariable Integer id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + id));
        producto.setActivo(true);
        producto.setFechaActualizacion(LocalDateTime.now());
        productoRepository.save(producto);
        return "redirect:/productos/inactivos";
    }
}
package com.example.DMC.controller;

import com.example.DMC.model.Categoria;
import com.example.DMC.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService service; // SIN CAMBIOS EN EL SERVICE

    // Listar categorías
    @GetMapping
    public String listarCategorias(Model model) {
        List<Categoria> categorias = service.findAll();
        model.addAttribute("categorias", categorias);
        model.addAttribute("newCategoria", new Categoria()); // para el modal de crear

        // Configuración para el layout (similar a ProductoController)
        model.addAttribute("view", "categoria"); // Apunta al fragmento 'categoria.html'
        model.addAttribute("activePage", "categorias");

        return "layout"; // Retorna el layout principal que cargará el fragmento "categoria"
    }

    // Guardar categoría (crear)
    @PostMapping("/guardar")
    public String guardarCategoria(@ModelAttribute("newCategoria") Categoria categoria,
            // Spring Binder se encarga de 'activo' cuando se usa th:field,
            // pero mantenemos @RequestParam activo como fallback si no se usa th:field.
            @RequestParam(value = "activo", defaultValue = "false") boolean activo,
            RedirectAttributes ra) {

        // Si usamos th:field="*{activo}" en el formulario, Spring ya lo establece en el
        // objeto 'categoria'.
        // Si no, lo establecemos manualmente (si el checkbox no se envía, se asume
        // 'false' por el hidden input).
        // Aunque no es estrictamente necesario debido a la estructura del form, es
        // seguro.
        categoria.setActivo(activo);

        service.save(categoria);
        ra.addFlashAttribute("mensaje", "Categoría creada correctamente.");
        return "redirect:/categorias";
    }

    // Actualizar categoría
    @PostMapping("/actualizar")
    public String actualizarCategoria(@RequestParam("idCategoria") Integer idCategoria,
            @RequestParam("nombre") String nombre,
            // Captura el valor del checkbox. Por el hidden input, si no se marca, llega
            // "false".
            @RequestParam(value = "activo", defaultValue = "false") boolean activo,
            RedirectAttributes ra) {

        Categoria cat = service.findById(idCategoria)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada: " + idCategoria));

        cat.setNombre(nombre);
        cat.setActivo(activo); // Se actualiza con el valor capturado del formulario

        service.save(cat);
        ra.addFlashAttribute("mensaje", "Categoría actualizada correctamente.");
        return "redirect:/categorias";
    }

    // Eliminar categoría
    @GetMapping("/eliminar/{id}")
    public String eliminarCategoria(@PathVariable Integer id, RedirectAttributes ra) {
        service.deleteById(id);
        ra.addFlashAttribute("mensaje", "Categoría eliminada.");
        return "redirect:/categorias";
    }

/* @Controller
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService service; // SIN CAMBIOS EN EL SERVICE

    // Listar categorías
    @GetMapping
    public String listarCategorias(Model model) {
        List<Categoria> categorias = service.findAll();
        model.addAttribute("categorias", categorias);
        model.addAttribute("newCategoria", new Categoria()); // para el modal de crear
        return "categoria"; // ajusta la ruta si usas otra carpeta
    }

    // Guardar categoría (crear)
    @PostMapping("/guardar")
    public String guardarCategoria(@ModelAttribute("newCategoria") Categoria categoria,
            @RequestParam(value = "activo", defaultValue = "false") boolean activo,
            RedirectAttributes ra) {
        categoria.setActivo(activo);
        service.save(categoria);
        ra.addFlashAttribute("mensaje", "Categoría creada correctamente.");
        return "redirect:/categorias";
    }

    // Actualizar categoría
    @PostMapping("/actualizar")
    public String actualizarCategoria(@RequestParam("idCategoria") Integer idCategoria,
            @RequestParam("nombre") String nombre,
            @RequestParam(value = "activo", defaultValue = "false") boolean activo,
            RedirectAttributes ra) {
        Categoria cat = service.findById(idCategoria)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada: " + idCategoria));

        cat.setNombre(nombre);
        cat.setActivo(activo);

        service.save(cat);
        ra.addFlashAttribute("mensaje", "Categoría actualizada correctamente.");
        return "redirect:/categorias";
    }

    // Eliminar categoría (igual que productos, vía GET)
    @GetMapping("/eliminar/{id}")
    public String eliminarCategoria(@PathVariable Integer id, RedirectAttributes ra) {
        service.deleteById(id);
        ra.addFlashAttribute("mensaje", "Categoría eliminada.");
        return "redirect:/categorias";
    } */
}

/* 
    @GetMapping
    @PreAuthorize("hasAnyRole('admin', 'CAJERO')")
    public List<Categoria> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('admin', 'CAJERO')")
    public ResponseEntity<Categoria> getById(@PathVariable Integer id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('admin')")
    public Categoria create(@RequestBody Categoria entity) {
        return service.save(entity);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Categoria> update(@PathVariable Integer id, @RequestBody Categoria entity) {
        return service.findById(id).map(existing -> {
            entity.setIdCategoria(id);
            return ResponseEntity.ok(service.save(entity));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
 */

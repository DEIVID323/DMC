package com.example.DMC.controller;

import com.example.DMC.model.*;
import com.example.DMC.repository.*;
import com.example.DMC.service.ExcelExportService;
import com.example.DMC.service.PdfExportService;

import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/reporte")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class ReporteController {

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final AlmacenRepository almacenRepository;
    private final ClienteRepository clienteRepository;
    private final ExcelExportService excelExportService;
    private final PdfExportService pdfExportService;

    // ========== MENÚ PRINCIPAL ==========
    @GetMapping
    public String menu(Model model) {
        model.addAttribute("view", "Reportes/menu");
        model.addAttribute("activePage", "reportes");
        return "layout";
    }

    // ========== REPORTE DE VENTAS ==========
    @GetMapping("/ventas")
    public String reporteVentas(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(required = false) String metodoPago,
            @RequestParam(required = false) Integer idCliente,
            Model model) {

        LocalDateTime inicio = (fechaInicio != null) ? fechaInicio.atStartOfDay() : LocalDateTime.now().minusMonths(1);
        LocalDateTime fin = (fechaFin != null) ? fechaFin.atTime(23, 59, 59) : LocalDateTime.now();

        // Obtener todas las ventas y filtrar
        List<Venta> ventas = ventaRepository.findAll().stream()
                .filter(v -> v.getFechaVenta().isAfter(inicio) && v.getFechaVenta().isBefore(fin))
                .filter(v -> metodoPago == null || metodoPago.isEmpty() || v.getMetodoPago().equalsIgnoreCase(metodoPago))
                .filter(v -> idCliente == null || (v.getIdCliente() != null && v.getIdCliente().equals(idCliente)))
                .collect(Collectors.toList());

        // Calcular estadísticas
        double totalVentas = ventas.stream()
                .mapToDouble(Venta::getTotalVenta)
                .sum();

        int cantidadVentas = ventas.size();

        double promedioVenta = cantidadVentas > 0 ? totalVentas / cantidadVentas : 0.0;

        model.addAttribute("ventas", ventas);
        model.addAttribute("totalVentas", totalVentas);
        model.addAttribute("cantidadVentas", cantidadVentas);
        model.addAttribute("promedioVenta", promedioVenta);
        model.addAttribute("clientes", clienteRepository.findAll());
        model.addAttribute("fechaInicio", fechaInicio != null ? fechaInicio : LocalDate.now().minusMonths(1));
        model.addAttribute("fechaFin", fechaFin != null ? fechaFin : LocalDate.now());
        model.addAttribute("metodoPago", metodoPago);
        model.addAttribute("idCliente", idCliente);
        model.addAttribute("view", "Reportes/ventas");
        model.addAttribute("activePage", "reportes");

        return "layout";
    }

    // ========== REPORTE DE INVENTARIO ==========
    @GetMapping("/inventario")
    public String reporteInventario(
            @RequestParam(required = false) Integer categoriaId,
            @RequestParam(required = false) Integer almacenId,
            @RequestParam(required = false) Boolean activo,
            @RequestParam(required = false) Integer stockMinimo,
            Model model) {

        List<Producto> productos = productoRepository.findAll().stream()
                .filter(p -> categoriaId == null || (p.getCategoria() != null && p.getCategoria().getIdCategoria().equals(categoriaId)))
                .filter(p -> activo == null || p.getActivo().equals(activo))
                .filter(p -> stockMinimo == null || p.getStock() <= stockMinimo)
                .collect(Collectors.toList());

        // Calcular estadísticas
        int totalProductos = productos.size();
        int stockTotal = productos.stream().mapToInt(Producto::getStock).sum();

        BigDecimal valorInventario = productos.stream()
                .map(p -> p.getPrecioVenta().multiply(BigDecimal.valueOf(p.getStock())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long productosConBajoStock = productos.stream()
                .filter(p -> p.getStock() <= 10)
                .count();

        // Productos por categoría
        Map<String, Long> productosPorCategoria = productos.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getCategoria() != null ? p.getCategoria().getNombre() : "Sin categoría",
                        Collectors.counting()
                ));

        model.addAttribute("productos", productos);
        model.addAttribute("totalProductos", totalProductos);
        model.addAttribute("stockTotal", stockTotal);
        model.addAttribute("valorInventario", valorInventario);
        model.addAttribute("productosConBajoStock", productosConBajoStock);
        model.addAttribute("productosPorCategoria", productosPorCategoria);
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("almacenes", almacenRepository.findAll());
        model.addAttribute("categoriaId", categoriaId);
        model.addAttribute("almacenId", almacenId);
        model.addAttribute("activo", activo);
        model.addAttribute("stockMinimo", stockMinimo);
        model.addAttribute("view", "Reportes/inventario");
        model.addAttribute("activePage", "reportes");

        return "layout";
    }

    // ========== EXPORTAR VENTAS A EXCEL ==========
    @GetMapping("/ventas/excel")
    public ResponseEntity<byte[]> exportarVentasExcel(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(required = false) String metodoPago) {

        LocalDateTime inicio = (fechaInicio != null) ? fechaInicio.atStartOfDay() : LocalDateTime.now().minusMonths(1);
        LocalDateTime fin = (fechaFin != null) ? fechaFin.atTime(23, 59, 59) : LocalDateTime.now();

        List<Venta> ventas = ventaRepository.findAll().stream()
                .filter(v -> v.getFechaVenta().isAfter(inicio) && v.getFechaVenta().isBefore(fin))
                .filter(v -> metodoPago == null || metodoPago.isEmpty() || v.getMetodoPago().equalsIgnoreCase(metodoPago))
                .collect(Collectors.toList());

        byte[] excelData = excelExportService.exportarVentas(ventas);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "reporte-ventas.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .body(excelData);
    }

    // ========== EXPORTAR VENTAS A PDF ==========
    @GetMapping("/ventas/pdf")
    public ResponseEntity<byte[]> exportarVentasPdf(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(required = false) String metodoPago) {

        LocalDateTime inicio = (fechaInicio != null) ? fechaInicio.atStartOfDay() : LocalDateTime.now().minusMonths(1);
        LocalDateTime fin = (fechaFin != null) ? fechaFin.atTime(23, 59, 59) : LocalDateTime.now();

        List<Venta> ventas = ventaRepository.findAll().stream()
                .filter(v -> v.getFechaVenta().isAfter(inicio) && v.getFechaVenta().isBefore(fin))
                .filter(v -> metodoPago == null || metodoPago.isEmpty() || v.getMetodoPago().equalsIgnoreCase(metodoPago))
                .collect(Collectors.toList());

        byte[] pdfData = pdfExportService.exportarVentas(ventas, fechaInicio, fechaFin);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "reporte-ventas.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfData);
    }

    // ========== EXPORTAR INVENTARIO A EXCEL ==========
    @GetMapping("/inventario/excel")
    public ResponseEntity<byte[]> exportarInventarioExcel(
            @RequestParam(required = false) Integer categoriaId,
            @RequestParam(required = false) Boolean activo) {

        List<Producto> productos = productoRepository.findAll().stream()
                .filter(p -> categoriaId == null || (p.getCategoria() != null && p.getCategoria().getIdCategoria().equals(categoriaId)))
                .filter(p -> activo == null || p.getActivo().equals(activo))
                .collect(Collectors.toList());

        byte[] excelData = excelExportService.exportarInventario(productos);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "reporte-inventario.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .body(excelData);
    }

    // ========== EXPORTAR INVENTARIO A PDF ==========
    @GetMapping("/inventario/pdf")
    public ResponseEntity<byte[]> exportarInventarioPdf(
            @RequestParam(required = false) Integer categoriaId,
            @RequestParam(required = false) Boolean activo) {

        List<Producto> productos = productoRepository.findAll().stream()
                .filter(p -> categoriaId == null || (p.getCategoria() != null && p.getCategoria().getIdCategoria().equals(categoriaId)))
                .filter(p -> activo == null || p.getActivo().equals(activo))
                .collect(Collectors.toList());

        byte[] pdfData = pdfExportService.exportarInventario(productos);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "reporte-inventario.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfData);
    }
}

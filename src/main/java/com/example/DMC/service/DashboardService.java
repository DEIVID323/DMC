package com.example.DMC.service;


import com.example.DMC.enums.EstadoVenta;
import com.example.DMC.model.DashboardData;
import com.example.DMC.model.Usuario;
import com.example.DMC.repository.CompraRepository;
import com.example.DMC.repository.ProductoRepository;
import com.example.DMC.repository.UsuarioRepository;
import com.example.DMC.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public DashboardData getDashboardData(String username) {
        DashboardData data = new DashboardData();

        // Obtener usuario de la base de datos
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));
        data.setUsuario(usuario);

        // Totales reales
        data.setTotalVentas(ventaRepository.countByEstado(EstadoVenta.completada));
        data.setTotalCompras(compraRepository.countByEstado("recibida")); // 👈 si Compras usa String, déjalo así
        data.setTotalProductos(productoRepository.countByActivo(true));
        data.setTotalProductosBajos(productoRepository.countByStockLessThanAndActivoTrue(5)); // Stock mínimo default 5

        // Datos para el gráfico (ventas de los últimos 5 días)
        LocalDate endDate = LocalDate.now();
        List<String> labelsVentas = new ArrayList<>();
        List<Integer> dataVentas = new ArrayList<>();

        for (int i = 4; i >= 0; i--) {
            LocalDate date = endDate.minusDays(i);
            labelsVentas.add(date.toString());

            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.plusDays(1).atStartOfDay();

            dataVentas.add((int) ventaRepository.countByFechaVentaBetweenAndEstado(
                    start,
                    end,
                    EstadoVenta.completada));
        }

        data.setLabelsVentas(labelsVentas);
        data.setDataVentas(dataVentas);

        return data;
    }
}
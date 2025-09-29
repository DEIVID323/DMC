package com.example.DMC.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.DMC.model.MovimientoInventario;
import com.example.DMC.model.Producto;
import com.example.DMC.model.Almacen;
import com.example.DMC.model.Usuario;
import com.example.DMC.enums.TipoMovimientoInventario;
import com.example.DMC.repository.MovimientoInventarioRepository;

@Service
public class MovimientoInventarioService {
    @Autowired
    private MovimientoInventarioRepository repository;

    @Autowired
    private ProductoService productoService;

    public List<MovimientoInventario> findAll() {
        return repository.findAll();
    }

    public List<MovimientoInventario> findAllOrderByFechaDesc() {
        return repository.findByOrderByFechaDesc();
    }

    public List<MovimientoInventario> findByProducto(Integer idProducto) {
        return repository.findByProductoIdProductoOrderByFechaDesc(idProducto);
    }

    public List<MovimientoInventario> findByTipoMovimiento(TipoMovimientoInventario tipoMovimiento) {
        return repository.findByTipoMovimientoOrderByFechaDesc(tipoMovimiento);
    }

    public List<MovimientoInventario> findByProductoAndAlmacen(Integer idProducto, Integer idAlmacen) {
        return repository.findByProductoAndAlmacenOrderByFechaDesc(idProducto, idAlmacen);
    }

    public Optional<MovimientoInventario> findById(Integer id) {
        return repository.findById(id);
    }

    public MovimientoInventario save(MovimientoInventario entity) {
        return repository.save(entity);
    }

    @Transactional
    public MovimientoInventario registrarMovimiento(Producto producto, Almacen almacen,
            TipoMovimientoInventario tipoMovimiento,
            Integer cantidad, Usuario usuario,
            Integer referenciaId, String observaciones) {

        // Validaciones
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }

        Integer stockAnterior = producto.getStock();
        Integer stockNuevo;

        // Calcular nuevo stock según el tipo de movimiento
        switch (tipoMovimiento) {
            case compra:
            case devolucion:
            case ajuste_positivo:
            case transferencia_entrada:
                stockNuevo = stockAnterior + cantidad;
                break;
            case venta:
            case ajuste_negativo:
            case transferencia_salida:
                if (stockAnterior < cantidad) {
                    throw new IllegalArgumentException("Stock insuficiente. Stock actual: " + stockAnterior);
                }
                stockNuevo = stockAnterior - cantidad;
                break;
            default:
                throw new IllegalArgumentException("Tipo de movimiento no válido");
        }

        // Crear el movimiento
        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setProducto(producto);
        movimiento.setAlmacen(almacen);
        movimiento.setTipoMovimiento(tipoMovimiento);
        movimiento.setCantidad(cantidad);
        movimiento.setStockAnterior(stockAnterior);
        movimiento.setStockNuevo(stockNuevo);
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setUsuario(usuario);
        movimiento.setReferenciaId(referenciaId);

        // Actualizar stock del producto
        producto.setStock(stockNuevo);
        productoService.save(producto);

        // Guardar el movimiento
        return repository.save(movimiento);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
    @Transactional
    public void

    realizarTransferencia(Producto producto, Almacen almacenOrigen, Almacen almacenDestino,
            Integer cantidad, Usuario usuario, String observaciones) {

        // Registrar salida en el almacén de origen
        this.registrarMovimiento(producto, almacenOrigen, TipoMovimientoInventario.transferencia_salida,
                cantidad, usuario, null, observaciones);

        // Registrar entrada en el almacén de destino
        this.registrarMovimiento(producto, almacenDestino, TipoMovimientoInventario.transferencia_entrada,
                cantidad, usuario, null, observaciones);
    }
    


}
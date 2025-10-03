package com.example.DMC.controller;

import com.example.DMC.enums.EstadoTurno;
import com.example.DMC.enums.TipoMovimiento;
import com.example.DMC.model.TurnoCaja;
import com.example.DMC.service.MovimientoCajaService;
import com.example.DMC.service.TurnoCajaService;
import com.example.DMC.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/caja")
@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CAJERO')")
public class CajaController {

    private final TurnoCajaService turnoCajaService;
    private final UsuarioService usuarioService;
    private final MovimientoCajaService movimientoCajaService;

    // Inyección por constructor (no requiere @Autowired)
    public CajaController(TurnoCajaService turnoCajaService,
            UsuarioService usuarioService,
            MovimientoCajaService movimientoCajaService) {
        this.turnoCajaService = turnoCajaService;
        this.usuarioService = usuarioService;
        this.movimientoCajaService = movimientoCajaService;
    }

    @GetMapping
    public String index(Authentication authentication, Model model) {
        try {
            String username = (authentication != null) ? authentication.getName() : "admin";
            var usuario = usuarioService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Turno abierto del usuario actual (puede ser null)
            TurnoCaja turnoAbierto = turnoCajaService.findTurnoAbiertoByUsuario(usuario.getIdUsuario());

            model.addAttribute("turnoAbierto", turnoAbierto);
            model.addAttribute("usuario", usuario);
            model.addAttribute("view", "Caja/caja");
            model.addAttribute("activePage", "caja");
            return "layout";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error al cargar la caja: " + e.getMessage());
            model.addAttribute("view", "error");
            return "layout";
        }
    }

    @PostMapping("/abrir")
    
    public String abrirCaja(@RequestParam("montoInicial") BigDecimal montoInicial,
            Authentication authentication,
            RedirectAttributes ra) {
        try {
            String username = (authentication != null) ? authentication.getName() : "admin";
            var usuario = usuarioService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Verificar turno abierto
            TurnoCaja turnoExistente = turnoCajaService.findTurnoAbiertoByUsuario(usuario.getIdUsuario());
            if (turnoExistente != null) {
                ra.addFlashAttribute("error", "Ya tienes un turno de caja abierto.");
                return "redirect:/caja";
            }

            TurnoCaja turno = new TurnoCaja();
            turno.setUsuario(usuario);
            turno.setFechaApertura(LocalDateTime.now());
            turno.setMontoInicial(montoInicial);
            turno.setEstado(EstadoTurno.abierto);

            turnoCajaService.save(turno);
            ra.addFlashAttribute("mensaje", "Caja abierta correctamente.");
            return "redirect:/caja";
        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute("error", "Error al abrir la caja: " + e.getMessage());
            return "redirect:/caja";
        }
    }

    @PostMapping("/cerrar")

    public String cerrarCaja(@RequestParam("idTurno") Integer idTurno,
            @RequestParam("montoFinalReal") BigDecimal montoFinalReal,
            RedirectAttributes ra) {
        try {
            TurnoCaja turno = turnoCajaService.findById(idTurno)
                    .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

            if (turno.getEstado() != EstadoTurno.abierto) {
                ra.addFlashAttribute("error", "El turno ya está cerrado.");
                return "redirect:/caja";
            }

            // Final del sistema: montoInicial + ventas - gastos
            BigDecimal montoFinalSistema = turnoCajaService.calcularMontoFinalSistema(turno);

            // Diferencia
            BigDecimal diferencia = montoFinalReal.subtract(montoFinalSistema);

            turno.setFechaCierre(LocalDateTime.now());
            turno.setMontoFinalSistema(montoFinalSistema);
            turno.setMontoFinalReal(montoFinalReal);
            turno.setDiferencia(diferencia);
            turno.setEstado(EstadoTurno.cerrado);

            turnoCajaService.save(turno);
            ra.addFlashAttribute("mensaje", "Caja cerrada correctamente. Diferencia: $" + diferencia);
            return "redirect:/caja";
        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute("error", "Error al cerrar la caja: " + e.getMessage());
            return "redirect:/caja";
        }
    }

    @GetMapping("/historial")
    public String historial(Authentication authentication, Model model) {
        try {
            String username = (authentication != null) ? authentication.getName() : "admin";
            var usuario = usuarioService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            List<TurnoCaja> turnos = turnoCajaService.findByUsuario(usuario.getIdUsuario());

            // Mapas para que el template no acceda a propiedades inexistentes del TurnoCaja
            Map<Integer, BigDecimal> ventasMap = new HashMap<>();
            Map<Integer, BigDecimal> gastosMap = new HashMap<>();
            Map<Integer, BigDecimal> finalSistemaMap = new HashMap<>();
            Map<Integer, BigDecimal> diferenciaMap = new HashMap<>();

            for (TurnoCaja t : turnos) {
                BigDecimal ventas = safe(movimientoCajaService.totalPorTipo(t.getIdTurno(), TipoMovimiento.ingreso));
                BigDecimal gastos = safe(movimientoCajaService.totalPorTipo(t.getIdTurno(), TipoMovimiento.egreso));
                BigDecimal finalSistema = t.getMontoInicial().add(ventas.subtract(gastos));
                BigDecimal diferencia = (t.getMontoFinalReal() != null)
                        ? t.getMontoFinalReal().subtract(finalSistema)
                        : null;

                ventasMap.put(t.getIdTurno(), ventas);
                gastosMap.put(t.getIdTurno(), gastos);
                finalSistemaMap.put(t.getIdTurno(), finalSistema);
                diferenciaMap.put(t.getIdTurno(), diferencia);
            }

            model.addAttribute("turnos", turnos);
            model.addAttribute("ventasMap", ventasMap);
            model.addAttribute("gastosMap", gastosMap);
            model.addAttribute("finalSistemaMap", finalSistemaMap);
            model.addAttribute("diferenciaMap", diferenciaMap);
            model.addAttribute("view", "Caja/cajahistorial");
            model.addAttribute("activePage", "caja");
            return "layout";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error al cargar el historial: " + e.getMessage());
            model.addAttribute("view", "error");
            return "layout";
        }
    }

    @GetMapping("/movimientos")
    public String movimientos(Authentication authentication, Model model) {
        try {
            String username = (authentication != null) ? authentication.getName() : "admin";
            var usuario = usuarioService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // (Opcional) podrías cargar aquí el turno abierto para la vista de movimientos
            TurnoCaja turnoAbierto = turnoCajaService.findTurnoAbiertoByUsuario(usuario.getIdUsuario());

            model.addAttribute("usuario", usuario);
            model.addAttribute("turnoAbierto", turnoAbierto);
            model.addAttribute("view", "Caja/movimientos");
            model.addAttribute("activePage", "caja");
            return "layout";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error al cargar los movimientos: " + e.getMessage());
            model.addAttribute("view", "error");
            return "layout";
        }
    }

    // ---------- helpers ----------
    private static BigDecimal safe(BigDecimal v) {
        return v != null ? v : BigDecimal.ZERO;
    }
}

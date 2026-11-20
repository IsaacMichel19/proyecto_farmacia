package app.utl.farmacia.Controller;

import app.utl.farmacia.DTO.VentasDTO;
import app.utl.farmacia.Services.VentasService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para registrar y consultar ventas.
 */
@RestController
@RequestMapping("/api/ventas")
public class VentasController {

    private final VentasService ventasService;

    public VentasController(VentasService ventasService) {
        this.ventasService = ventasService;
    }

    @GetMapping
    public ResponseEntity<List<VentasDTO>> listarTodas() {
        return ResponseEntity.ok(ventasService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentasDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ventasService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<VentasDTO> registrarVenta(@RequestBody VentasDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ventasService.registrarVenta(dto));
    }
}
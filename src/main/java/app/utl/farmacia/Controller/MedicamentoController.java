package app.utl.farmacia.Controller;

import app.utl.farmacia.DTO.MedicamentoDTO;
import app.utl.farmacia.Services.MedicamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para el catálogo de medicamentos.
 */
@RestController
@RequestMapping("/api/medicamentos")
public class MedicamentoController {

    private final MedicamentoService medicamentoService;

    public MedicamentoController(MedicamentoService medicamentoService) {
        this.medicamentoService = medicamentoService;
    }

    @GetMapping
    public ResponseEntity<List<MedicamentoDTO>> listarTodos() {
        return ResponseEntity.ok(medicamentoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicamentoDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(medicamentoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<MedicamentoDTO> crear(@RequestBody MedicamentoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(medicamentoService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicamentoDTO> actualizar(@PathVariable Long id, @RequestBody MedicamentoDTO dto) {
        return ResponseEntity.ok(medicamentoService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        medicamentoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
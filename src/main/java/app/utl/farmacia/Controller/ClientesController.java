package app.utl.farmacia.Controller;

import app.utl.farmacia.DTO.ClientesDTO;
import app.utl.farmacia.Services.ClientesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para clientes.
 * Buena práctica: mantiene los controladores ligeros, delegando la lógica al Service
 * y devolviendo códigos HTTP adecuados (200 OK, 201 Created, 204 No Content).
 */
@RestController
@RequestMapping("/api/clientes")
public class ClientesController {

    private final ClientesService clientesService;

    public ClientesController(ClientesService clientesService) {
        this.clientesService = clientesService;
    }

    @GetMapping
    public ResponseEntity<List<ClientesDTO>> listarTodos() {
        return ResponseEntity.ok(clientesService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientesDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clientesService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<ClientesDTO> crear(@RequestBody ClientesDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientesService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientesDTO> actualizar(@PathVariable Long id, @RequestBody ClientesDTO dto) {
        return ResponseEntity.ok(clientesService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        clientesService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
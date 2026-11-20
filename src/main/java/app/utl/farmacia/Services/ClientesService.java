package app.utl.farmacia.Services;

import app.utl.farmacia.DTO.ClientesDTO;
import app.utl.farmacia.Entities.Clientes;
import app.utl.farmacia.Excepciones.RecursoNoEncontradoException;
import app.utl.farmacia.Excepciones.ReglaNegocioException;
import app.utl.farmacia.Repositories.ClientesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Capa de servicio para la lógica de negocio de clientes.
 * Buena práctica: valida datos, lanza excepciones de negocio y convierte entre Entity y DTO.
 */
@Service
public class ClientesService {

    private final ClientesRepository clientesRepository;

    public ClientesService(ClientesRepository clientesRepository) {
        this.clientesRepository = clientesRepository;
    }

    public List<ClientesDTO> listarTodos() {
        return clientesRepository.buscarTodos().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public ClientesDTO obtenerPorId(Long id) {
        Clientes cliente = buscarEntidadPorId(id);
        return convertirADTO(cliente);
    }

    public ClientesDTO crear(ClientesDTO dto) {
        validarDatosCliente(dto);
        // Normalizamos espacios con trim() para evitar registros sucios
        Clientes entidad = new Clientes(null, dto.getNombre().trim(), dto.getTelefono().trim());
        Clientes guardado = clientesRepository.guardar(entidad);
        return convertirADTO(guardado);
    }

    public ClientesDTO actualizar(Long id, ClientesDTO dto) {
        validarDatosCliente(dto);
        Clientes existente = buscarEntidadPorId(id);
        existente.setNombre(dto.getNombre().trim());
        existente.setTelefono(dto.getTelefono().trim());
        clientesRepository.actualizar(existente);
        return convertirADTO(existente);
    }

    public void eliminar(Long id) {
        buscarEntidadPorId(id); // Verifica que exista antes de borrar
        clientesRepository.eliminarPorId(id);
    }

    // Método reutilizable por otros servicios (como VentasService) para validar existencia
    public Clientes buscarEntidadPorId(Long id) {
        return clientesRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con ID: " + id));
    }

    private void validarDatosCliente(ClientesDTO dto) {
        if (dto == null || dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new ReglaNegocioException("El nombre del cliente es obligatorio.");
        }
        if (dto.getTelefono() == null || dto.getTelefono().isBlank()) {
            throw new ReglaNegocioException("El teléfono del cliente es obligatorio.");
        }
    }

    private ClientesDTO convertirADTO(Clientes entidad) {
        return new ClientesDTO(entidad.getId(), entidad.getNombre(), entidad.getTelefono());
    }
}
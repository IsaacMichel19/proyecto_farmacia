package app.utl.farmacia.Services;

import app.utl.farmacia.DTO.MedicamentoDTO;
import app.utl.farmacia.Entities.Medicamento;
import app.utl.farmacia.Excepciones.RecursoNoEncontradoException;
import app.utl.farmacia.Excepciones.ReglaNegocioException;
import app.utl.farmacia.Repositories.MedicamentoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Capa de servicio para la lógica de negocio de medicamentos e inventario.
 */
@Service
public class MedicamentoService {

    private final MedicamentoRepository medicamentoRepository;

    public MedicamentoService(MedicamentoRepository medicamentoRepository) {
        this.medicamentoRepository = medicamentoRepository;
    }

    public List<MedicamentoDTO> listarTodos() {
        return medicamentoRepository.buscarTodos().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public MedicamentoDTO obtenerPorId(Long id) {
        Medicamento medicamento = buscarEntidadPorId(id);
        return convertirADTO(medicamento);
    }

    public MedicamentoDTO crear(MedicamentoDTO dto) {
        validarDatosMedicamento(dto);
        Medicamento entidad = convertirAEntidad(dto);
        entidad.setId(null); // Asegura que la BD asigne un nuevo ID
        Medicamento guardado = medicamentoRepository.guardar(entidad);
        return convertirADTO(guardado);
    }

    public MedicamentoDTO actualizar(Long id, MedicamentoDTO dto) {
        validarDatosMedicamento(dto);
        buscarEntidadPorId(id);
        Medicamento entidad = convertirAEntidad(dto);
        entidad.setId(id);
        medicamentoRepository.actualizar(entidad);
        return convertirADTO(entidad);
    }

    public void eliminar(Long id) {
        buscarEntidadPorId(id);
        medicamentoRepository.eliminarPorId(id);
    }

    public Medicamento buscarEntidadPorId(Long id) {
        return medicamentoRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Medicamento no encontrado con ID: " + id));
    }

    private void validarDatosMedicamento(MedicamentoDTO dto) {
        if (dto == null || dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new ReglaNegocioException("El nombre del medicamento es obligatorio.");
        }
        if (dto.getPrecio() == null || dto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ReglaNegocioException("El precio debe ser mayor a cero.");
        }
        if (dto.getStock() == null || dto.getStock() < 0) {
            throw new ReglaNegocioException("El stock no puede ser negativo.");
        }
        if (dto.getCategoriaId() == null) {
            throw new ReglaNegocioException("La categoría es obligatoria.");
        }
    }

    private Medicamento convertirAEntidad(MedicamentoDTO dto) {
        return new Medicamento(
                dto.getId(),
                dto.getNombre().trim(),
                dto.getPrincipioActivo() != null ? dto.getPrincipioActivo().trim() : null,
                dto.getPrecio(),
                dto.getStock(),
                dto.getRequiereReceta() != null ? dto.getRequiereReceta() : Boolean.FALSE,
                dto.getCategoriaId()
        );
    }

    private MedicamentoDTO convertirADTO(Medicamento entidad) {
        return new MedicamentoDTO(
                entidad.getId(),
                entidad.getNombre(),
                entidad.getPrincipioActivo(),
                entidad.getPrecio(),
                entidad.getStock(),
                entidad.getRequiereReceta(),
                entidad.getCategoriaId()
        );
    }
}
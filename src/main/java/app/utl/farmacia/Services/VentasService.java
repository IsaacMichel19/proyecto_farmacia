package app.utl.farmacia.Services;

import app.utl.farmacia.DTO.VentasDTO;
import app.utl.farmacia.Entities.Ventas;
import app.utl.farmacia.Excepciones.RecursoNoEncontradoException;
import app.utl.farmacia.Excepciones.ReglaNegocioException;
import app.utl.farmacia.Repositories.MedicamentoRepository;
import app.utl.farmacia.Repositories.VentasRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Capa de servicio para procesar ventas.
 * Modificado por: Brayan
 */
@Service
public class VentasService {

    private final VentasRepository ventasRepository;
    private final ClientesService clientesService;
    private final MedicamentoService medicamentoService;
    private final MedicamentoRepository medicamentoRepository;

    public VentasService(VentasRepository ventasRepository,
                         ClientesService clientesService,
                         MedicamentoService medicamentoService,
                         MedicamentoRepository medicamentoRepository) {
        this.ventasRepository = ventasRepository;
        this.clientesService = clientesService;
        this.medicamentoService = medicamentoService;
        this.medicamentoRepository = medicamentoRepository;
    }

    public List<VentasDTO> listarTodas() {
        return ventasRepository.buscarTodas().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public VentasDTO obtenerPorId(Long id) {
        Ventas venta = buscarEntidadPorId(id);
        return convertirADTO(venta);
    }

    // --- Método requerido por VentasController (@PostMapping) ---

    public VentasDTO registrarVenta(VentasDTO dto) {
        if (dto == null) {
            throw new ReglaNegocioException("Los datos de la venta son obligatorios");
        }
        int cantidad = dto.getCantidad() != null ? dto.getCantidad() : 0;
        validarCantidad(cantidad);

        Ventas entidad = new Ventas();
        entidad.setClienteId(dto.getClienteId());
        entidad.setMedicamentoId(dto.getMedicamentoId());
        entidad.setCantidad(cantidad);
        entidad.setTotal(dto.getTotal());
        entidad.setFechaVenta(dto.getFechaVenta());

        Ventas guardada = ventasRepository.guardar(entidad);
        return convertirADTO(guardada);
    }

    // --- Métodos de lógica agregados por Brayan ---

    public BigDecimal calcularTotalVenta(BigDecimal precioUnitario, int cantidad) {
        validarCantidad(cantidad);
        if (precioUnitario == null || precioUnitario.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ReglaNegocioException("El precio unitario debe ser mayor a 0");
        }
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }

    public void validarCantidad(int cantidad) {
        if (cantidad <= 0) {
            throw new ReglaNegocioException("La cantidad de la venta debe ser mayor a 0");
        }
    }

    private Ventas buscarEntidadPorId(Long id) {
        return ventasRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Venta no encontrada con ID: " + id));
    }

    private VentasDTO convertirADTO(Ventas entidad) {
        if (entidad == null) {
            return null;
        }
        VentasDTO dto = new VentasDTO();
        dto.setId(entidad.getId());
        dto.setClienteId(entidad.getClienteId());
        dto.setMedicamentoId(entidad.getMedicamentoId());
        dto.setCantidad(entidad.getCantidad());
        dto.setTotal(entidad.getTotal());
        dto.setFechaVenta(entidad.getFechaVenta());
        return dto;
    }
}
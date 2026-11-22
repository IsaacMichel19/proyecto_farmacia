package app.utl.farmacia.Services;

import app.utl.farmacia.DTOs.VentasDTO;
import app.utl.farmacia.Excepciones.RecursoNoEncontradoException;
import app.utl.farmacia.Excepciones.ReglaNegocioException;
import app.utl.farmacia.Models.Ventas;
import app.utl.farmacia.Repositories.MedicamentoRepository;
import app.utl.farmacia.Repositories.VentasRepository;
import org.springframework.stereotype.Service;

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

    // --- Métodos de lógica agregados por Brayan ---

    public double calcularTotalVenta(double precioUnitario, int cantidad) {
        validarCantidad(cantidad);
        if (precioUnitario <= 0) {
            throw new ReglaNegocioException("El precio unitario debe ser mayor a 0");
        }
        return precioUnitario * cantidad;
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
        VentasDTO dto = new VentasDTO();
        dto.setId(entidad.getId());
        dto.setClienteId(entidad.getClienteId());
        dto.setMedicamentoId(entidad.getMedicamentoId());
        dto.setCantidad(entidad.getCantidad());
        dto.setTotal(entidad.getTotal());
        return dto;
    }
}
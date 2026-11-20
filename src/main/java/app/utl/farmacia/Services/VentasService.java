package app.utl.farmacia.Services;

import app.utl.farmacia.DTO.VentasDTO;
import app.utl.farmacia.Entities.Medicamento;
import app.utl.farmacia.Entities.Ventas;
import app.utl.farmacia.Excepciones.RecursoNoEncontradoException;
import app.utl.farmacia.Excepciones.ReglaNegocioException;
import app.utl.farmacia.Repositories.MedicamentoRepository;
import app.utl.farmacia.Repositories.VentasRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Capa de servicio para procesar ventas.
 * Buena práctica: usa @Transactional para que si falla algún paso, no quede inventario a medias.
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
        Ventas venta = ventasRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Venta no encontrada con ID: " + id));
        return convertirADTO(venta);
    }

    @Transactional
    public VentasDTO registrarVenta(VentasDTO dto) {
        if (dto == null || dto.getClienteId() == null || dto.getMedicamentoId() == null) {
            throw new ReglaNegocioException("El cliente y el medicamento son obligatorios.");
        }
        if (dto.getCantidad() == null || dto.getCantidad() <= 0) {
            throw new ReglaNegocioException("La cantidad debe ser mayor a cero.");
        }

        // 1. Verifica que el cliente exista
        clientesService.buscarEntidadPorId(dto.getClienteId());

        // 2. Verifica que el medicamento exista y tenga stock suficiente
        Medicamento medicamento = medicamentoService.buscarEntidadPorId(dto.getMedicamentoId());
        if (medicamento.getStock() < dto.getCantidad()) {
            throw new ReglaNegocioException(
                    "Stock insuficiente para '" + medicamento.getNombre() +
                            "'. Disponible: " + medicamento.getStock()
            );
        }

        // 3. Calcula el total en el backend y descuenta el inventario
        BigDecimal total = medicamento.getPrecio().multiply(BigDecimal.valueOf(dto.getCantidad()));
        int nuevoStock = medicamento.getStock() - dto.getCantidad();
        medicamentoRepository.actualizarStock(medicamento.getId(), nuevoStock);

        // 4. Registra la venta con la fecha/hora actual del servidor
        Ventas venta = new Ventas(
                null,
                dto.getClienteId(),
                dto.getMedicamentoId(),
                dto.getCantidad(),
                total,
                LocalDateTime.now()
        );
        Ventas ventaGuardada = ventasRepository.guardar(venta);

        return convertirADTO(ventaGuardada);
    }

    private VentasDTO convertirADTO(Ventas entidad) {
        return new VentasDTO(
                entidad.getId(),
                entidad.getClienteId(),
                entidad.getMedicamentoId(),
                entidad.getCantidad(),
                entidad.getTotal(),
                entidad.getFechaVenta()
        );
    }
}
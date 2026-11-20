package app.utl.farmacia.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para solicitar el registro de ventas y retornar su detalle.
 */
public class VentasDTO {

    private Long id;
    private Long clienteId;
    private Long medicamentoId;
    private Integer cantidad;
    private BigDecimal total;
    private LocalDateTime fechaVenta;

    public VentasDTO() {
    }

    public VentasDTO(Long id, Long clienteId, Long medicamentoId, Integer cantidad,
                     BigDecimal total, LocalDateTime fechaVenta) {
        this.id = id;
        this.clienteId = clienteId;
        this.medicamentoId = medicamentoId;
        this.cantidad = cantidad;
        this.total = total;
        this.fechaVenta = fechaVenta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getMedicamentoId() {
        return medicamentoId;
    }

    public void setMedicamentoId(Long medicamentoId) {
        this.medicamentoId = medicamentoId;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public LocalDateTime getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDateTime fechaVenta) {
        this.fechaVenta = fechaVenta;
    }
}
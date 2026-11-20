package app.utl.farmacia.DTO;

import java.math.BigDecimal;

/**
 * DTO para recibir y devolver datos de medicamentos en los endpoints REST.
 */
public class MedicamentoDTO {

    private Long id;
    private String nombre;
    private String principioActivo;
    private BigDecimal precio;
    private Integer stock;
    private Boolean requiereReceta;
    private Long categoriaId;

    public MedicamentoDTO() {
    }

    public MedicamentoDTO(Long id, String nombre, String principioActivo, BigDecimal precio,
                          Integer stock, Boolean requiereReceta, Long categoriaId) {
        this.id = id;
        this.nombre = nombre;
        this.principioActivo = principioActivo;
        this.precio = precio;
        this.stock = stock;
        this.requiereReceta = requiereReceta;
        this.categoriaId = categoriaId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrincipioActivo() {
        return principioActivo;
    }

    public void setPrincipioActivo(String principioActivo) {
        this.principioActivo = principioActivo;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Boolean getRequiereReceta() {
        return requiereReceta;
    }

    public void setRequiereReceta(Boolean requiereReceta) {
        this.requiereReceta = requiereReceta;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }
}
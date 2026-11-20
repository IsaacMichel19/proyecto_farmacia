package app.utl.farmacia.DTO;

/**
 * DTO (Data Transfer Object) para transportar información de clientes entre capas.
 * Buena práctica: separa los datos que entran/salen de la API respecto a la entidad interna.
 */
public class ClientesDTO {

    private Long id;
    private String nombre;
    private String telefono;

    public ClientesDTO() {
    }

    public ClientesDTO(Long id, String nombre, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
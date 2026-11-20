package app.utl.farmacia.Entities;

/**
 * Entidad que modela los datos de un cliente de la farmacia.
 * Buena práctica: atributos privados encapsulados con getters y setters.
 */
public class Clientes {

    private Long id;
    private String nombre;
    private String telefono;

    // Constructor vacío requerido para instanciación por frameworks o row mappers
    public Clientes() {
    }

    // Constructor completo para crear instancias de forma directa y limpia
    public Clientes(Long id, String nombre, String telefono) {
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
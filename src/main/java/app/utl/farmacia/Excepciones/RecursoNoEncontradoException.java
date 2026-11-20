package app.utl.farmacia.Excepciones;

/**
 * Excepción lanzada cuando un recurso (cliente, medicamento, venta) no existe.
 */
public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
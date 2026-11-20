package app.utl.farmacia.Excepciones;

/**
 * Excepción lanzada al violar validaciones o reglas del negocio.
 */
public class ReglaNegocioException extends RuntimeException {
    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
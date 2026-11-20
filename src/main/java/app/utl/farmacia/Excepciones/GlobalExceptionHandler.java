package app.utl.farmacia.Excepciones;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Maneja los errores de la API de forma sencilla y centralizada.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Cuando no se encuentra un registro (404)
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<String> noEncontrado(RecursoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // Cuando falla una regla de negocio, como falta de stock (400)
    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<String> reglaNegocio(ReglaNegocioException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    // Cualquier otro error inesperado (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> errorGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno: " + ex.getMessage());
    }
}
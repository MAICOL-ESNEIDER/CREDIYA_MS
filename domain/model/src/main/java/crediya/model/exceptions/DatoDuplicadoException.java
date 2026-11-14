package crediya.model.exceptions;

/**
 * Se lanza cuando se intenta guardar un dato que ya existe y no se puede repetir.
 */
public class DatoDuplicadoException extends RuntimeException {

    public DatoDuplicadoException(String mensaje) {
        super(mensaje);
    }
}

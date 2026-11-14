package crediya.model.exceptions;

/**
 * Se lanza cuando el correo electrónico ya está registrado por otro usuario.
 */
public class CorreoDuplicadoException extends DatoDuplicadoException {

    public CorreoDuplicadoException(String correo) {
        super("El correo electrónico " + correo + " ya está registrado");
    }
}

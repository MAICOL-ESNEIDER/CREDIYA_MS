package crediya.api.exception;

import crediya.api.dto.RespuestaError;
import crediya.model.exceptions.CorreoDuplicadoException;
import crediya.model.exceptions.ValidacionException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void validacionRespondeBadRequestConLaListaDeErrores() {
        ResponseEntity<RespuestaError> respuesta =
                handler.manejarValidacion(new ValidacionException(List.of("El campo nombres es obligatorio")));

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertEquals(List.of("El campo nombres es obligatorio"), respuesta.getBody().errores());
    }

    @Test
    void duplicadoRespondeConflict() {
        ResponseEntity<RespuestaError> respuesta =
                handler.manejarDuplicado(new CorreoDuplicadoException("maicol@correo.com"));

        assertEquals(HttpStatus.CONFLICT, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertEquals(409, respuesta.getBody().estado());
    }

    @Test
    void errorInesperadoRespondeInternalServerErrorSinDetallesInternos() {
        ResponseEntity<RespuestaError> respuesta = handler.manejarInesperado(new IllegalStateException("detalle interno"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertEquals("Ocurrió un error inesperado. Intente de nuevo más tarde", respuesta.getBody().mensaje());
    }
}

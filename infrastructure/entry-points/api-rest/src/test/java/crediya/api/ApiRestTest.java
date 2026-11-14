package crediya.api;

import crediya.api.dto.UsuarioRequest;
import crediya.api.dto.UsuarioResponse;
import crediya.model.user.User;
import crediya.usecase.user.UserUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ApiRestTest {

    private final UserUseCase userUseCase = mock(UserUseCase.class);
    private final ApiRest apiRest = new ApiRest(userUseCase);

    @Test
    void registrarUsuarioRespondeCreadoConLosDatosGuardados() {
        UsuarioRequest request = new UsuarioRequest("Maicol", "Posada", LocalDate.of(2000, 5, 10),
                "Calle 10", "3001234567", "maicol@correo.com", "1000123456", new BigDecimal("2500000"));
        when(userUseCase.registrarUsuario(any(User.class)))
                .thenAnswer(invocacion -> ((User) invocacion.getArgument(0)).toBuilder().id(1L).idRol(3L).build());

        ResponseEntity<UsuarioResponse> respuesta = apiRest.registrarUsuario(request);

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertEquals(1L, respuesta.getBody().id());
        assertEquals("maicol@correo.com", respuesta.getBody().correoElectronico());
        assertEquals(3L, respuesta.getBody().idRol());
    }
}

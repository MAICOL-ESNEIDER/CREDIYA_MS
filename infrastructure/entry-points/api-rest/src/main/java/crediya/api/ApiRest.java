package crediya.api;

import crediya.api.dto.RespuestaError;
import crediya.api.dto.UsuarioRequest;
import crediya.api.dto.UsuarioResponse;
import crediya.model.user.User;
import crediya.usecase.user.UserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

/**
 * Controlador REST (entry point): recibe las peticiones HTTP y se las pasa al caso de uso.
 */
@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Registro de usuarios de CrediYa")
public class ApiRest {

    private static final Logger LOG = Logger.getLogger(ApiRest.class.getName());

    private final UserUseCase userUseCase;

    @PostMapping(path = "/usuarios", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Registrar un nuevo usuario",
            description = "Valida los datos, verifica que el correo no exista y guarda el usuario con rol CLIENTE.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario registrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos",
                    content = @Content(schema = @Schema(implementation = RespuestaError.class))),
            @ApiResponse(responseCode = "409", description = "Correo o documento ya registrado",
                    content = @Content(schema = @Schema(implementation = RespuestaError.class))),
            @ApiResponse(responseCode = "500", description = "Error inesperado",
                    content = @Content(schema = @Schema(implementation = RespuestaError.class)))
    })
    public ResponseEntity<UsuarioResponse> registrarUsuario(@RequestBody UsuarioRequest request) {
        LOG.info("Petición recibida: POST /api/v1/usuarios");

        User registrado = userUseCase.registrarUsuario(request.toDomain());

        LOG.info(() -> "Respuesta enviada: usuario creado con id " + registrado.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioResponse.fromDomain(registrado));
    }
}

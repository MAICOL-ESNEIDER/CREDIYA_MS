package crediya.usecase.user;

import crediya.model.exceptions.CorreoDuplicadoException;
import crediya.model.exceptions.ValidacionException;
import crediya.model.rol.gateways.RolRepository;
import crediya.model.user.User;
import crediya.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Caso de uso de la HU1: registrar un usuario.
 * Aquí viven las reglas del negocio (criterios de aceptación).
 */
@RequiredArgsConstructor
public class UserUseCase {

    private static final Logger LOG = Logger.getLogger(UserUseCase.class.getName());
    private static final Pattern FORMATO_CORREO =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final BigDecimal SALARIO_MINIMO = BigDecimal.ZERO;
    private static final BigDecimal SALARIO_MAXIMO = new BigDecimal("15000000");
    private static final String ROL_POR_DEFECTO = "CLIENTE";

    private final UserRepository userRepository;
    private final RolRepository rolRepository;

    public User registrarUsuario(User user) {
        LOG.info("Iniciando el registro de un nuevo usuario");

        validarDatos(user);

        String correo = user.getCorreoElectronico().trim().toLowerCase(Locale.ROOT);
        if (userRepository.existsByCorreoElectronico(correo)) {
            LOG.warning(() -> "Registro rechazado: el correo " + correo + " ya existe");
            throw new CorreoDuplicadoException(correo);
        }

        Long idRolCliente = rolRepository.buscarIdPorNombre(ROL_POR_DEFECTO)
                .orElseThrow(() -> new IllegalStateException("No existe el rol " + ROL_POR_DEFECTO));

        User usuarioNuevo = user.toBuilder()
                .id(null)
                .nombres(user.getNombres().trim())
                .apellidos(user.getApellidos().trim())
                .correoElectronico(correo)
                .idRol(idRolCliente)
                .build();

        User guardado = userRepository.save(usuarioNuevo);
        LOG.info(() -> "Usuario registrado correctamente con id " + guardado.getId());
        return guardado;
    }

    private void validarDatos(User user) {
        if (user == null) {
            throw new ValidacionException(List.of("No se enviaron datos del usuario"));
        }

        List<String> errores = new ArrayList<>();

        if (estaVacio(user.getNombres())) {
            errores.add("El campo nombres es obligatorio");
        }
        if (estaVacio(user.getApellidos())) {
            errores.add("El campo apellidos es obligatorio");
        }

        if (estaVacio(user.getCorreoElectronico())) {
            errores.add("El campo correo_electronico es obligatorio");
        } else if (!FORMATO_CORREO.matcher(user.getCorreoElectronico().trim()).matches()) {
            errores.add("El correo_electronico no tiene un formato válido");
        }

        BigDecimal salario = user.getSalarioBase();
        if (salario == null) {
            errores.add("El campo salario_base es obligatorio");
        } else if (salario.compareTo(SALARIO_MINIMO) < 0 || salario.compareTo(SALARIO_MAXIMO) > 0) {
            errores.add("El salario_base debe estar entre 0 y 15000000");
        }

        if (!errores.isEmpty()) {
            LOG.warning(() -> "Datos inválidos en el registro: " + errores);
            throw new ValidacionException(errores);
        }
    }

    private boolean estaVacio(String valor) {
        return valor == null || valor.isBlank();
    }
}

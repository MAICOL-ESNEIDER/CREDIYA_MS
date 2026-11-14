package crediya.usecase.user;

import crediya.model.exceptions.CorreoDuplicadoException;
import crediya.model.exceptions.ValidacionException;
import crediya.model.rol.gateways.RolRepository;
import crediya.model.user.User;
import crediya.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserUseCaseTest {

    private static final Long ID_ROL_CLIENTE = 3L;

    private UserRepository userRepository;
    private RolRepository rolRepository;
    private UserUseCase useCase;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        rolRepository = mock(RolRepository.class);
        useCase = new UserUseCase(userRepository, rolRepository);
        when(rolRepository.buscarIdPorNombre("CLIENTE")).thenReturn(Optional.of(ID_ROL_CLIENTE));
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocacion -> ((User) invocacion.getArgument(0)).toBuilder().id(1L).build());
    }

    private User usuarioValido() {
        return User.builder()
                .nombres(" Maicol ")
                .apellidos("Posada")
                .correoElectronico("Maicol@Correo.com")
                .salarioBase(new BigDecimal("2500000"))
                .build();
    }

    @Test
    void registraUsuarioValidoConRolClienteYCorreoEnMinusculas() {
        User resultado = useCase.registrarUsuario(usuarioValido());

        assertEquals(1L, resultado.getId());
        assertEquals("Maicol", resultado.getNombres());
        assertEquals("maicol@correo.com", resultado.getCorreoElectronico());
        assertEquals(ID_ROL_CLIENTE, resultado.getIdRol());
    }

    @Test
    void rechazaCamposObligatoriosVacios() {
        User vacio = User.builder().nombres(" ").build();

        ValidacionException error = assertThrows(ValidacionException.class, () -> useCase.registrarUsuario(vacio));

        assertEquals(4, error.getErrores().size());
        verify(userRepository, never()).save(any());
    }

    @Test
    void rechazaCorreoConFormatoInvalido() {
        User usuario = usuarioValido().toBuilder().correoElectronico("correo-sin-arroba").build();

        ValidacionException error = assertThrows(ValidacionException.class, () -> useCase.registrarUsuario(usuario));

        assertTrue(error.getErrores().contains("El correo_electronico no tiene un formato válido"));
    }

    @Test
    void rechazaSalarioMayorAlMaximo() {
        User usuario = usuarioValido().toBuilder().salarioBase(new BigDecimal("15000001")).build();

        assertThrows(ValidacionException.class, () -> useCase.registrarUsuario(usuario));
    }

    @Test
    void rechazaSalarioNegativo() {
        User usuario = usuarioValido().toBuilder().salarioBase(new BigDecimal("-1")).build();

        assertThrows(ValidacionException.class, () -> useCase.registrarUsuario(usuario));
    }

    @Test
    void aceptaSalariosEnLosLimites() {
        User conCero = usuarioValido().toBuilder().salarioBase(BigDecimal.ZERO).build();
        User conMaximo = usuarioValido().toBuilder().salarioBase(new BigDecimal("15000000")).build();

        assertEquals(1L, useCase.registrarUsuario(conCero).getId());
        assertEquals(1L, useCase.registrarUsuario(conMaximo).getId());
    }

    @Test
    void rechazaCorreoYaRegistrado() {
        when(userRepository.existsByCorreoElectronico("maicol@correo.com")).thenReturn(true);
        User usuario = usuarioValido();

        assertThrows(CorreoDuplicadoException.class, () -> useCase.registrarUsuario(usuario));
        verify(userRepository, never()).save(any());
    }

    @Test
    void fallaSiNoExisteElRolCliente() {
        when(rolRepository.buscarIdPorNombre("CLIENTE")).thenReturn(Optional.empty());
        User usuario = usuarioValido();

        assertThrows(IllegalStateException.class, () -> useCase.registrarUsuario(usuario));
    }

    @Test
    void rechazaUsuarioNulo() {
        ValidacionException error = assertThrows(ValidacionException.class, () -> useCase.registrarUsuario(null));

        assertEquals(1, error.getErrores().size());
    }
}

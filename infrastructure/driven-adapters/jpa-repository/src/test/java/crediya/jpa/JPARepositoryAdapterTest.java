package crediya.jpa;

import crediya.jpa.entity.UserEntity;
import crediya.model.exceptions.DatoDuplicadoException;
import crediya.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JPARepositoryAdapterTest {

    private JPARepository repository;
    private JPARepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        repository = mock(JPARepository.class);
        adapter = new JPARepositoryAdapter(repository, mock(ObjectMapper.class));
    }

    @Test
    void guardaElUsuarioYDevuelveElIdGenerado() {
        when(repository.save(any(UserEntity.class))).thenAnswer(invocacion -> {
            UserEntity entidad = invocacion.getArgument(0);
            entidad.setId(10L);
            return entidad;
        });
        User usuario = User.builder()
                .nombres("Maicol")
                .apellidos("Posada")
                .correoElectronico("maicol@correo.com")
                .salarioBase(new BigDecimal("2500000"))
                .idRol(3L)
                .build();

        User guardado = adapter.save(usuario);

        assertEquals(10L, guardado.getId());
        assertEquals("maicol@correo.com", guardado.getCorreoElectronico());
        assertEquals(3L, guardado.getIdRol());
    }

    @Test
    void convierteElErrorDeDuplicadoDeLaBaseDeDatos() {
        when(repository.save(any(UserEntity.class))).thenThrow(new DataIntegrityViolationException("duplicado"));
        User usuario = User.builder().correoElectronico("maicol@correo.com").build();

        assertThrows(DatoDuplicadoException.class, () -> adapter.save(usuario));
    }

    @Test
    void consultaSiElCorreoExiste() {
        when(repository.existsByCorreoElectronico("maicol@correo.com")).thenReturn(true);

        assertTrue(adapter.existsByCorreoElectronico("maicol@correo.com"));
    }
}

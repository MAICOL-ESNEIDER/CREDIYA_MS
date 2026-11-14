package crediya.jpa;

import crediya.jpa.entity.RolEntity;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RolRepositoryAdapterTest {

    private final RolJPARepository rolJPARepository = mock(RolJPARepository.class);
    private final RolRepositoryAdapter adapter = new RolRepositoryAdapter(rolJPARepository);

    @Test
    void devuelveElIdDelRolCuandoExiste() {
        when(rolJPARepository.findByNombre("CLIENTE"))
                .thenReturn(Optional.of(new RolEntity(3L, "CLIENTE", "Solicita préstamos")));

        assertEquals(Optional.of(3L), adapter.buscarIdPorNombre("CLIENTE"));
    }

    @Test
    void devuelveVacioCuandoElRolNoExiste() {
        when(rolJPARepository.findByNombre("OTRO")).thenReturn(Optional.empty());

        assertTrue(adapter.buscarIdPorNombre("OTRO").isEmpty());
    }
}

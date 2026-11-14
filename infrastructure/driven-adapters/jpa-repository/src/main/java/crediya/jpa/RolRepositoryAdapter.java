package crediya.jpa;

import crediya.jpa.entity.RolEntity;
import crediya.model.rol.gateways.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Adaptador que implementa el puerto RolRepository consultando la tabla "rol".
 */
@Repository
@RequiredArgsConstructor
public class RolRepositoryAdapter implements RolRepository {

    private final RolJPARepository rolJPARepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Long> buscarIdPorNombre(String nombre) {
        return rolJPARepository.findByNombre(nombre).map(RolEntity::getId);
    }
}

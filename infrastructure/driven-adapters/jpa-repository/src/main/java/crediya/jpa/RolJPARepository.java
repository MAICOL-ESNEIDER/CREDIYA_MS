package crediya.jpa;

import crediya.jpa.entity.RolEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Repositorio de Spring Data para la tabla "rol".
 */
public interface RolJPARepository extends CrudRepository<RolEntity, Long> {

    Optional<RolEntity> findByNombre(String nombre);
}

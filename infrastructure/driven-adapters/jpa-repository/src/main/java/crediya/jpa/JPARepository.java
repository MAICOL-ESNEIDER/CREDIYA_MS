package crediya.jpa;

import crediya.jpa.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * Repositorio de Spring Data para la tabla "usuario".
 * Spring escribe el SQL solo, a partir del nombre de cada método.
 */
public interface JPARepository extends CrudRepository<UserEntity, Long>, QueryByExampleExecutor<UserEntity> {

    boolean existsByCorreoElectronico(String correoElectronico);
}

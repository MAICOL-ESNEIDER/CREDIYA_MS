package crediya.model.user.gateways;

import crediya.model.user.User;

/**
 * Puerto (gateway) del dominio: dice QUÉ se puede hacer con los usuarios guardados,
 * pero no CÓMO. El "cómo" lo implementa el adaptador de base de datos (infraestructura).
 */
public interface UserRepository {

    User save(User user);

    boolean existsByCorreoElectronico(String correoElectronico);
}

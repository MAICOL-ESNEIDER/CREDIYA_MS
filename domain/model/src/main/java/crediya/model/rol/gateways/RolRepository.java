package crediya.model.rol.gateways;

import java.util.Optional;

/**
 * Puerto (gateway) del dominio para consultar los roles del sistema.
 */
public interface RolRepository {

    Optional<Long> buscarIdPorNombre(String nombre);
}

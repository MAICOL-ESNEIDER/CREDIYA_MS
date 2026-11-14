package crediya.jpa;

import crediya.jpa.entity.UserEntity;
import crediya.jpa.helper.AdapterOperations;
import crediya.model.exceptions.DatoDuplicadoException;
import crediya.model.user.User;
import crediya.model.user.gateways.UserRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Logger;

/**
 * Adaptador (driven adapter): implementa el puerto UserRepository del dominio usando JPA y PostgreSQL.
 * Traduce entre el modelo del dominio (User) y la entidad de la base de datos (UserEntity).
 */
@Repository
public class JPARepositoryAdapter extends AdapterOperations<User, UserEntity, Long, JPARepository>
        implements UserRepository {

    private static final Logger LOG = Logger.getLogger(JPARepositoryAdapter.class.getName());

    public JPARepositoryAdapter(JPARepository repository, ObjectMapper mapper) {
        super(repository, mapper, JPARepositoryAdapter::aDominio);
    }

    @Override
    @Transactional
    public User save(User user) {
        try {
            User guardado = super.save(user);
            LOG.info(() -> "Usuario guardado en la base de datos con id " + guardado.getId());
            return guardado;
        } catch (DataIntegrityViolationException e) {
            LOG.warning("La base de datos rechazó el registro por un dato duplicado");
            throw new DatoDuplicadoException("Ya existe un usuario con el mismo correo o documento de identidad");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCorreoElectronico(String correoElectronico) {
        return repository.existsByCorreoElectronico(correoElectronico);
    }

    @Override
    protected UserEntity toData(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .nombres(user.getNombres())
                .apellidos(user.getApellidos())
                .fechaNacimiento(user.getFechaNacimiento())
                .direccion(user.getDireccion())
                .telefono(user.getTelefono())
                .correoElectronico(user.getCorreoElectronico())
                .documentoIdentidad(user.getDocumentoIdentidad())
                .salarioBase(user.getSalarioBase())
                .idRol(user.getIdRol())
                .build();
    }

    private static User aDominio(UserEntity entity) {
        return User.builder()
                .id(entity.getId())
                .nombres(entity.getNombres())
                .apellidos(entity.getApellidos())
                .fechaNacimiento(entity.getFechaNacimiento())
                .direccion(entity.getDireccion())
                .telefono(entity.getTelefono())
                .correoElectronico(entity.getCorreoElectronico())
                .documentoIdentidad(entity.getDocumentoIdentidad())
                .salarioBase(entity.getSalarioBase())
                .idRol(entity.getIdRol())
                .build();
    }
}

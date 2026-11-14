package crediya.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import crediya.model.user.User;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de salida: el JSON que la API responde cuando el usuario se registra bien.
 */
public record UsuarioResponse(
        @JsonProperty("id") Long id,
        @JsonProperty("nombres") String nombres,
        @JsonProperty("apellidos") String apellidos,
        @JsonProperty("fecha_nacimiento") LocalDate fechaNacimiento,
        @JsonProperty("direccion") String direccion,
        @JsonProperty("telefono") String telefono,
        @JsonProperty("correo_electronico") String correoElectronico,
        @JsonProperty("documento_identidad") String documentoIdentidad,
        @JsonProperty("salario_base") BigDecimal salarioBase,
        @JsonProperty("id_rol") Long idRol) {

    public static UsuarioResponse fromDomain(User user) {
        return new UsuarioResponse(
                user.getId(),
                user.getNombres(),
                user.getApellidos(),
                user.getFechaNacimiento(),
                user.getDireccion(),
                user.getTelefono(),
                user.getCorreoElectronico(),
                user.getDocumentoIdentidad(),
                user.getSalarioBase(),
                user.getIdRol());
    }
}

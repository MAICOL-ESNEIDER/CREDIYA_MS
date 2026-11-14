package crediya.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import crediya.model.user.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de entrada: el JSON que el cliente envía en el POST /api/v1/usuarios.
 */
@Schema(description = "Datos para registrar un nuevo usuario")
public record UsuarioRequest(
        @Schema(example = "Maicol Esneider") @JsonProperty("nombres") String nombres,
        @Schema(example = "Posada Callejas") @JsonProperty("apellidos") String apellidos,
        @Schema(example = "2000-05-10") @JsonProperty("fecha_nacimiento") LocalDate fechaNacimiento,
        @Schema(example = "Calle 10 # 20-30") @JsonProperty("direccion") String direccion,
        @Schema(example = "3001234567") @JsonProperty("telefono") String telefono,
        @Schema(example = "maicol@correo.com") @JsonProperty("correo_electronico") String correoElectronico,
        @Schema(example = "1000123456") @JsonProperty("documento_identidad") String documentoIdentidad,
        @Schema(example = "2500000") @JsonProperty("salario_base") BigDecimal salarioBase) {

    public User toDomain() {
        return User.builder()
                .nombres(nombres)
                .apellidos(apellidos)
                .fechaNacimiento(fechaNacimiento)
                .direccion(direccion)
                .telefono(telefono)
                .correoElectronico(correoElectronico)
                .documentoIdentidad(documentoIdentidad)
                .salarioBase(salarioBase)
                .build();
    }
}

package pe.interseguro.siv.common.dto.request;

import java.io.Serializable;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)

public class SolicitudGuardarBeneficiarioRequestDTO implements Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -4693695298034368755L;
	private String tipoBeneficiario;
    @NotBlank(message = "Campo requerido")
    private String nombres;
    @NotBlank(message = "Campo requerido")
    private String apellidoPaterno;
    @NotBlank(message = "Campo requerido")
    private String apellidoMaterno;
    @NotBlank(message = "Campo requerido")
    private String fechaNacimiento;
    @NotBlank(message = "Campo requerido")
    private String tipoDocumento;
    @NotBlank(message = "Campo requerido")
    private String numeroDocumento;
    @NotBlank(message = "Campo requerido")
    private String tipoRelacion;
    private Integer distribucion;

}

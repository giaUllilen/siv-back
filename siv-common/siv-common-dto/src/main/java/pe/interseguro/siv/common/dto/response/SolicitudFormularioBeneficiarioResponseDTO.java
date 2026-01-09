package pe.interseguro.siv.common.dto.response;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)

public class SolicitudFormularioBeneficiarioResponseDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 669757376367823047L;
	
	private String tipoBeneficiario;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String fechaNacimiento;
    private String tipoDocumento;
    private String numeroDocumento;
    private String tipoRelacion;
    private Integer distribucion;

}

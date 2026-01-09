package pe.interseguro.siv.common.bean;

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
public class SolicitudPDFBeneficiarioBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6136443895864384439L;

	private String tipoBeneficiario;
	private String nombres;
	private String apellidoPaterno;
	private String apellidoMaterno;
	private String fechaNacimiento;
	private String tipoDocumento;
	private String numeroDocumento;
	private String relacion;
	private String distribucion;
}

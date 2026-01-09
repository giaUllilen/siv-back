package pe.interseguro.siv.common.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObtenerPersonaDocumentoResponseDTO extends BaseResponseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String ID_PERSONA;
	
	private String ID_TIPO_DOCUMENTO;
	
	private String TIPO_DOCUMENTO;
	
	private String NUMERO_DOCUMENTO;
	
	private String NOMBRE;
	
	private String APELLIDO_PATERNO;
	
	private String APELLIDO_MARNO;
	
	private String FECHA_NACIMIENTO;
}

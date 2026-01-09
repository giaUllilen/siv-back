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

public class SolicitudGuardarPersonaResponseDTO implements Serializable{
	
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 5634361829515695065L;
	private Long idPersona;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String razonSocial;
    private String tipoDocumento;
    private String numeroDocumento;
    private String celular;

}

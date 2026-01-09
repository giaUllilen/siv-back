package pe.interseguro.siv.common.dto.response;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class UsuarioIngresoResponseDTO extends BaseResponseDTO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -287968792481843972L;
	
	private String jwtToken;
	private String idUsuario;
	private String nombreUsuario;
	private String nombreVendedor;
	private String apellidoVendedor;
	private String idUsuarioCRM;
	private String codigoAgenciaCRM;
	private String codigoVendedorCRM;
	private String correoUsuario;
	private Perfil perfil;
	private Boolean esAgenteSolicitud;
	
}

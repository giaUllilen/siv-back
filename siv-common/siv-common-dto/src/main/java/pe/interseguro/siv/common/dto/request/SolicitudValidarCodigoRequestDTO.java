package pe.interseguro.siv.common.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class SolicitudValidarCodigoRequestDTO extends BaseRequestDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6180996905093728660L;
	protected Long idSolicitud;
	protected String usuarioLogin;
	protected String codigoAsegurado;
	protected String codigoContratante;
}

package pe.interseguro.siv.common.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class SolicitudReglamentoResponseDTO extends BaseResponseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5320812948788556507L;
	
	private String idSolicitud;
	private String numeroCotizacion;
	private Boolean esNuevo;
	private SolicitudFormularioCobroResponseDTO cobro;
	private String muerteAccidental;
}

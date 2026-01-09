package pe.interseguro.siv.common.dto.request;

import java.util.Date;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class SolicitudRequestDTO extends BaseRequestDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8861919619109324298L;
	
	protected Long idSolicitud;
	protected String numeroPoliza;
	protected String tipoDocumento;
	protected String numeroDocumento;
	protected String cliente;
	protected String subPlan;
	protected Date fechaSolicitud;
	protected String estado;
	protected int codigoEstado;
}

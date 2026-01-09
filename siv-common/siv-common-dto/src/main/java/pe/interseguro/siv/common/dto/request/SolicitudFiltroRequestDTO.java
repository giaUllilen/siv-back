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
public class SolicitudFiltroRequestDTO extends BaseRequestDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6500566342401768610L;
	
	protected Long idSolicitud;
	protected String numeroPropuesta;
	protected String numeroPoliza;
	protected String tipoDocumento;
	protected String numeroDocumento;
	protected String cliente;
	protected String subPlan;
	protected Date fechaSolicitud;
	protected int estado;
	protected String estadoNombre;
	protected PaginationRequestDTO pagination;
}

package pe.interseguro.siv.common.dto.response;

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
public class SolicitudItemResponseDTO {
		
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
	
}

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

public class SolicitudGuardarResponseDTO extends BaseResponseDTO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1999239791011741025L;
	
	private Long idSolicitud;
	private String numeroCotizacion;
	private SolicitudGuardarPersonaResponseDTO asegurado;
	private String aseguradoIgualContratante;
	private SolicitudGuardarPersonaResponseDTO contratante;
    private String fechaFirmaAsegurado;
    private String fechaFirmaContratante;
    private String estadoSolicitud;

}

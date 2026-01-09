package pe.interseguro.siv.common.dto.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CotizadorCrmParticipacionDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5163643056385659932L;
	
	private String idTercero;
    private String idRol;
    private String idDireccion;
    private String numeroDocumento;
}

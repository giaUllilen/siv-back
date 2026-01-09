package pe.interseguro.siv.common.persistence.rest.crm.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrmParticipacion implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2045091855130458891L;
	
	private String idTercero;
    private String idRol;
    private String idDireccion;
    private String numeroDocumento;
}

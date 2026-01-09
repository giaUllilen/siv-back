package pe.interseguro.siv.common.persistence.rest.vidafree.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsultaCotizacionRequest implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5774287053675287003L;
	
	private String numeroDocumento;
}

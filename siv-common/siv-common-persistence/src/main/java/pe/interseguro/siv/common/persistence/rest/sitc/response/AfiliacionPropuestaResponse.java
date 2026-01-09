package pe.interseguro.siv.common.persistence.rest.sitc.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AfiliacionPropuestaResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2764576008392083679L;
	
	private String codigoRespuesta;
	private String mensajeRespuesta;
	private String idAfiliacionPropuesta;
}

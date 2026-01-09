package pe.interseguro.siv.common.persistence.rest.crm.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenericoResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1706653807260715789L;
	
	private boolean respuesta;
	private String mensaje;
}

package pe.interseguro.siv.common.persistence.rest.vidafree.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AgenteResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3961058738465236768L;

	private String id;
	private String nombre;
}


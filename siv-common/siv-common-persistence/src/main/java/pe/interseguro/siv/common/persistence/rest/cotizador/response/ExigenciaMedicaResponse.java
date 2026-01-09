package pe.interseguro.siv.common.persistence.rest.cotizador.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExigenciaMedicaResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7802026323525084916L;
	
	private String item;
	private String nombreExigenciaMedica;
}

package pe.interseguro.siv.common.persistence.rest.bupo.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AsignarProspectoResponse implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8143774760668500446L;
	
	private String asginacion;
	private String mensaje;
	
}

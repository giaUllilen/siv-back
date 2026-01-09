package pe.interseguro.siv.common.persistence.rest.global.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidarPlaftResponse implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8143774760668500446L;
	
	private String numero_poliza;
	private String fecha_emision;
	
}

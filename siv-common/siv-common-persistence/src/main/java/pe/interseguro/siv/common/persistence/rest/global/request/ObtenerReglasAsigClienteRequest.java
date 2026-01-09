package pe.interseguro.siv.common.persistence.rest.global.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ObtenerReglasAsigClienteRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String numDoc;
	private String correo;
	private String numCotizacion;
}

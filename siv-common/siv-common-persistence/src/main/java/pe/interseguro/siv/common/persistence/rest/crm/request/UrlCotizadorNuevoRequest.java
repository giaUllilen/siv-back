package pe.interseguro.siv.common.persistence.rest.crm.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UrlCotizadorNuevoRequest implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 963045773345994163L;
	
	String strIdOportunidad;
}

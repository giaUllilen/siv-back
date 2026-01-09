package pe.interseguro.siv.common.persistence.rest.cotizador.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AsegurableCrmResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1571366459655755192L;
	
	private int err;
	private String id;
	private String msg;
	private Boolean rs;
	private String idCrm;
}

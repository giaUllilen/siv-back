package pe.interseguro.siv.common.persistence.rest.cotizador.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AsegurableResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -280066110938918645L;
	
	private String profid;
	private String edadactual;
	private String flaigv;
	private String sexo;
	private String nomasegurable;
	private String cotnum;
	private String fecnac;
	private String cotfec;
	private String tipofum;
}

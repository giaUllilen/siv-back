package pe.interseguro.siv.common.persistence.rest.sitc.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenSitcRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 244424353023815561L;

	private String numPropuesta;
	private String numIp;
	private String usuario;
	private String usuarioAplicacion;
}

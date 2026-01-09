package pe.interseguro.siv.common.persistence.rest.cotizador.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import pe.interseguro.siv.common.persistence.rest.niubiz.response.BaseResponse;

@Getter
@Setter
public class ObtenerCorrelativoResponse extends BaseResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8391643758362139226L;
	
	private String err;
	private String id;
	private String msg;
	private String rs;
}

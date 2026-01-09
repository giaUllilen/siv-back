package pe.interseguro.siv.common.persistence.rest.global.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import pe.interseguro.siv.common.persistence.rest.niubiz.response.BaseResponse;

@Getter
@Setter
public class TransmitirTraspasoResponse extends BaseResponse implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String mensaje;
	private boolean success;
}

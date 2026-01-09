package pe.interseguro.siv.common.persistence.rest.global.response;

import java.io.Serializable;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import pe.interseguro.siv.common.persistence.rest.niubiz.response.BaseResponse;
import pe.interseguro.siv.common.persistence.rest.vtigger.response.ActualizarContactoResponse;

@Getter
@Setter
@Data
public class ActualizarContactoVtigerResponse extends BaseResponse implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String mensaje;
	private boolean success;
	
}

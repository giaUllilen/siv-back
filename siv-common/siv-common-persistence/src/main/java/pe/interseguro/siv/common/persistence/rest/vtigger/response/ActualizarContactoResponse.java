package pe.interseguro.siv.common.persistence.rest.vtigger.response;

import java.io.Serializable;

import lombok.Data;
import pe.interseguro.siv.common.persistence.rest.niubiz.response.BaseResponse;

@Data
public class ActualizarContactoResponse extends BaseResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8552686272841223251L;

	private boolean success;
}

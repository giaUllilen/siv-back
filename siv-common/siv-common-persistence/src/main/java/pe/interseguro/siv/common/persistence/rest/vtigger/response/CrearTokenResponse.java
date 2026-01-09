package pe.interseguro.siv.common.persistence.rest.vtigger.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import pe.interseguro.siv.common.persistence.rest.niubiz.response.BaseResponse;

@Setter
@Getter
public class CrearTokenResponse extends BaseResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6916824260810214231L;
	
	private boolean success;
	private CrearTokenResultResponse result;
}
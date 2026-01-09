package pe.interseguro.siv.common.persistence.rest.vtigger.response;

import java.io.Serializable;

import lombok.Data;
import pe.interseguro.siv.common.persistence.rest.niubiz.response.BaseResponse;

@Data
public class CrearSessionResponse extends BaseResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5338271698127522063L;
	
	private boolean success;
	private CrearSessionResultResponse result;
}

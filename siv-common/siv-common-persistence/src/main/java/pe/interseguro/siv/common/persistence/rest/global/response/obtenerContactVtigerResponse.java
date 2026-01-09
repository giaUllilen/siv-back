package pe.interseguro.siv.common.persistence.rest.global.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import pe.interseguro.siv.common.persistence.rest.niubiz.response.BaseResponse;

@Getter
@Setter
public class obtenerContactVtigerResponse  extends BaseResponse implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("tipodocumento")
	private String tipodocumento;
	
	@JsonProperty("nrodocumento")
	private String nrodocumento;
	
	@JsonProperty("Mensaje")
	private String Mensaje;
	
	

}

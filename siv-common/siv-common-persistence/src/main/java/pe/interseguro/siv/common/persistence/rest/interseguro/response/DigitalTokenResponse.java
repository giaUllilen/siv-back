package pe.interseguro.siv.common.persistence.rest.interseguro.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import pe.interseguro.siv.common.persistence.rest.interseguro.request.DigitalTokenDetResponse;
import pe.interseguro.siv.common.persistence.rest.niubiz.response.BaseResponse;

@Getter
@Setter
public class DigitalTokenResponse extends BaseResponse implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -776885437640905225L;

	private Boolean success;
	private String mensaje;
	private String extra;
	private DigitalTokenDetResponse item;
	private String items;
	
}

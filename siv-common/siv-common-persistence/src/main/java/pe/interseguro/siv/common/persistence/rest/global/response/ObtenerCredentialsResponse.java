package pe.interseguro.siv.common.persistence.rest.global.response;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObtenerCredentialsResponse implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("code")
	private String code;
	
	@JsonProperty("title")
	private String title;
	
	@JsonProperty("message")
	private String message;
	
	@JsonProperty("estado")
	private String estado;
	
	@JsonProperty("data")
	private List<ObtenerDataResponse> data; 

}

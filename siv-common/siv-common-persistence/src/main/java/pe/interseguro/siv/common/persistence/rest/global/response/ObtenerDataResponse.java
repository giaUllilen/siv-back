package pe.interseguro.siv.common.persistence.rest.global.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObtenerDataResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("name")
	private String name;
	
	@JsonProperty("apiKey")
	private String apiKey;	

}

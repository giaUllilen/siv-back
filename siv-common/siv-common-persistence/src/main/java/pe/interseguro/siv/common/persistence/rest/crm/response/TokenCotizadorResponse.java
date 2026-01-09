package pe.interseguro.siv.common.persistence.rest.crm.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenCotizadorResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5210504624191630274L;

	@JsonProperty("msg")
	private String mensaje;
	
	@JsonProperty("rs")
	private boolean respuesta;
	
	private String url;

}

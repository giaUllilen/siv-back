package pe.interseguro.siv.common.persistence.rest.crm.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenCotizadorRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6364142011434740801L;
	
	
	@JsonProperty("strIdOportunidad")
	private String idOportunidad;
}

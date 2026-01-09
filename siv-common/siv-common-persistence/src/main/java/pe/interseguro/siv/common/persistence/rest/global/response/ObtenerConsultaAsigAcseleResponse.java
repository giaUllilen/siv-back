package pe.interseguro.siv.common.persistence.rest.global.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObtenerConsultaAsigAcseleResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("numCot")
	private String numCot;
	
	@JsonProperty("rpta")
	private String rpta;
	
	@JsonProperty("codAgencia")
	private String codAgencia;
	
	
	@JsonProperty("correo")
	private String correo;
}

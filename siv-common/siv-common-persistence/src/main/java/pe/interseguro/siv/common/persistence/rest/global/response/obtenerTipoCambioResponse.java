package pe.interseguro.siv.common.persistence.rest.global.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class obtenerTipoCambioResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("tipoCambio")
	private Double tipoCambio;
	
	@JsonProperty("monedainidesc")
	private String monedainidesc;
	
	@JsonProperty("monedafindesc")
	private String monedafindesc;
	
	@JsonProperty("fecha")
	private String fecha;
	
}

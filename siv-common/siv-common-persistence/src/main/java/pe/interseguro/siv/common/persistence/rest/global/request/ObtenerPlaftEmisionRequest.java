package pe.interseguro.siv.common.persistence.rest.global.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ObtenerPlaftEmisionRequest implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("tipoDocumentoContratante")
	private String tipoDocumentoContratante;
	
	@JsonProperty("tipoDocumentoAsegurado")
	private String tipoDocumentoAsegurado;
	
	@JsonProperty("periodo")
	private String periodo;
	
	@JsonProperty("tipoCambio")
	private String tipoCambio;
	
	@JsonProperty("prima")
	private String prima;
	
	@JsonProperty("moneda")
	private String moneda;
	
	@JsonProperty("nacionalidadCon")
	private String nacionalidadCon;
	
	@JsonProperty("nacionalidadAse")
	private String nacionalidadAse;
}

package pe.interseguro.siv.common.persistence.rest.global.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObtenerReglasAsigClienteResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("CodigoAgente")
	private String CodigoAgente;
	
	@JsonProperty("Usuario")
	private String Usuario;
	
	@JsonProperty("Nom_agente")
	private String Nom_agente;
	
	@JsonProperty("Estado_agente")
	private String Estado_agente;
	
	@JsonProperty("Origen")
	private String Origen;
	
	@JsonProperty("fecha_crea")
	private String fecha_crea;
	
	@JsonProperty("Mensaje")
	private String Mensaje;
	
	@JsonProperty("flg_transfer")
	private String flg_transfer;
	
}

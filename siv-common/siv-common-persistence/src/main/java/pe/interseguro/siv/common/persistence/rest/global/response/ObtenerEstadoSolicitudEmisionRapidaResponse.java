package pe.interseguro.siv.common.persistence.rest.global.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObtenerEstadoSolicitudEmisionRapidaResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8143774760668500446L;
	
	@JsonProperty("codigo")
	private String codigo;
	
	@JsonProperty("estado")
	private String estado;
}

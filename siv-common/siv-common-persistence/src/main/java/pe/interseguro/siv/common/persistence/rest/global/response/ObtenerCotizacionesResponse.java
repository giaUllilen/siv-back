package pe.interseguro.siv.common.persistence.rest.global.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObtenerCotizacionesResponse  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("tipo_producto")
	private String tipo_producto;
	
	@JsonProperty("numero_propuesta")
	private String numero_propuesta;
	
	@JsonProperty("numero_cotizacion")
	private String numero_cotizacion;
	
	@JsonProperty("estado")
	private String estado;
	
	@JsonProperty("estado_general")
	private String estado_general;
}


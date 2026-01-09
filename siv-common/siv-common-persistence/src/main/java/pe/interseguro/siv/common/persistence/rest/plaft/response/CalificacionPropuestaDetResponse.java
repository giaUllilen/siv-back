package pe.interseguro.siv.common.persistence.rest.plaft.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalificacionPropuestaDetResponse {
	@JsonProperty("imprimible")
	private Integer imprimible;
	
	@JsonProperty("descripcion")
	private String descripcion;
}

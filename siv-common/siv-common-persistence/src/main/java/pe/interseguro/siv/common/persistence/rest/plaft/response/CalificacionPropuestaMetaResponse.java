package pe.interseguro.siv.common.persistence.rest.plaft.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalificacionPropuestaMetaResponse {
	@JsonProperty("status")
	private String status;
	
	@JsonProperty("count")
	private Integer count;
}

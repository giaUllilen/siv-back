package pe.interseguro.siv.common.persistence.rest.plaft.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaftTokenMetaResponse {
	@JsonProperty("status")
	private String status;
	
	@JsonProperty("count")
	private String count;
	
}

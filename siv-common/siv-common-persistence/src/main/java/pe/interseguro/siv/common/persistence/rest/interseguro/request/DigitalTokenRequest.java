package pe.interseguro.siv.common.persistence.rest.interseguro.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DigitalTokenRequest {
	@JsonProperty("grant_type")
	private String grantType;
	
	@JsonProperty("client_id")
	private String cliendId;
	
	@JsonProperty("client_secret")
	private String clientSecret;
}

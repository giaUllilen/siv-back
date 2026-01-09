package pe.interseguro.siv.common.persistence.rest.interseguro.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AzmanTokenResponse {
	@JsonProperty("access_token")
	private String accessToken;
	
	@JsonProperty("token_type")
	private String tokenType;
	
	@JsonProperty("expires_in")
	private String expiresIn;
	
	@JsonProperty("refresh_token")
	private String refreshToken;
	
	@JsonProperty("as:client_id")
	private String asClientId;
	
	@JsonProperty("userName")
	private String userName;
	
	@JsonProperty(".issued")
	private String issued;
	
	@JsonProperty(".expires")
	private String expires;
	
}

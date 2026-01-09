package pe.interseguro.siv.common.persistence.rest.indenova.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CrearTokenRequest implements Serializable {

	@JsonProperty("ClientId")
	private String clientId;
	
	@JsonProperty("Secret")
	private String secret;
	
	@JsonProperty("userIduserId")
	private String userId;

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
		
	
}

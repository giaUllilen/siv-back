package pe.interseguro.siv.common.persistence.rest.niubiz.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessionDataResponse {
	private String sessionKey;
	private Long expirationTime;
}

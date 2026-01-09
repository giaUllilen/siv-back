package pe.interseguro.siv.common.persistence.rest.niubiz.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class NiubizTokenResponse {
	private String token;
	private String codigo;
	private String mensajeError;
	private String errorHttp;
}

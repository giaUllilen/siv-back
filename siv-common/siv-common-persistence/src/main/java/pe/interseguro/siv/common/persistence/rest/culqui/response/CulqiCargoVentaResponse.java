package pe.interseguro.siv.common.persistence.rest.culqui.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class CulqiCargoVentaResponse implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 2594412871204106316L;
	
	@JsonProperty("type")
	private String tipo;
	@JsonProperty("code")
	private String codigo;
	@JsonProperty("merchant_message")
	private String mensajeComercio;
	@JsonProperty("user_message")
	private String mensajeUsuario;
}

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
public class CulqiTokenTarjetaResponse implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 2552043096079879504L;

	@JsonProperty("object")
	private String objecto;
	@JsonProperty("bin")
	private String bin;
	@JsonProperty("card_brand")
	private String operador;
	@JsonProperty("card_type")
	private String tipo;
	@JsonProperty("card_category")
	private String categoria;
	@JsonProperty("issuer")
	private CulqiTokenTarjetaBancoResponse banco;
	
}

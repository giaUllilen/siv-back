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
public class CulqiTokenTarjetaBancoResponse implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1961402635997360841L;

	@JsonProperty("name")
	private String nombre;
	@JsonProperty("country")
	private String pais;
	@JsonProperty("country_code")
	private String codigoPais;
	@JsonProperty("website")
	private String sitioWeb;
}

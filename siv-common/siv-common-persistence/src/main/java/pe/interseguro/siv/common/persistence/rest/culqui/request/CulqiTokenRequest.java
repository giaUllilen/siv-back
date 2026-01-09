package pe.interseguro.siv.common.persistence.rest.culqui.request;

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
public class CulqiTokenRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8948680951892230327L;

	@JsonProperty("card_number")
	private String numeroTarjeta;
	private String cvv;
	private String email;
	@JsonProperty("expiration_month")
	private String mesExpiracion;
	@JsonProperty("expiration_year")
	private String anioExpiracion;
}

package pe.interseguro.siv.common.persistence.rest.culqui.response;

import java.io.Serializable;
import java.util.Map;

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
public class CulqiTokenResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7992634493891475240L;

	@JsonProperty("object")
	private String objeto;
	@JsonProperty("id")
	private String token;
	@JsonProperty("type")
	private String tipo;
	@JsonProperty("code")
	private String codigo;
	@JsonProperty("merchant_message")
	private String mensajeComercial;
	@JsonProperty("user_message")
	private String usuarioComercial;
	@JsonProperty("param")
	private String parametro;
	@JsonProperty("creation_date")
	private String fechaCreacion;
	@JsonProperty("email")
	private String email;
	@JsonProperty("card_number")
	private String numeroTarjeta;
	@JsonProperty("last_four")
	private String ultimosDigitos;
	@JsonProperty("active")
	private String activo;
	@JsonProperty("iin")
	private CulqiTokenTarjetaResponse tarjeta;
	@JsonProperty("client")
	private CulqiTokenDispositivoResponse dispositivo;
	@JsonProperty("timestamp")
	private String fechaEvento;
	@JsonProperty("status")
	private String estadoPeticion;
	@JsonProperty("error")
	private String errorHttp;
	@JsonProperty("message")
	private String mensajeError;
}

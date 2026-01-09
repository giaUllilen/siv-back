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
public class CulqiCargoResponse implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = -8916915390409976010L;

	@JsonProperty("code")
	private String codigo;
	@JsonProperty("object")
	private String objeto;
	@JsonProperty("id")
	private String idCargo;
	@JsonProperty("type")
	private String tipo;
	@JsonProperty("creation_date")
	private String fechaTransaccion;
	@JsonProperty("amount")
	private String monto;
	@JsonProperty("currency_code")
	private String moneda;
	@JsonProperty("outcome")
	private CulqiCargoVentaResponse informacionVenta;
	@JsonProperty("merchant_message")
	private String mensajeComercio;
	@JsonProperty("user_message")
	private String mensajeUsuario;
	@JsonProperty("param")
	private String parametro;
	@JsonProperty("error")
	private String errorHttp;
	@JsonProperty("message")
	private String mensajeError;
}

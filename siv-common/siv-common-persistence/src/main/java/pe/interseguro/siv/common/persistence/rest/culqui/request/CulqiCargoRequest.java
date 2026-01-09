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
public class CulqiCargoRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7220290167918079786L;
	
	@JsonProperty("amount")
	private String monto;
	@JsonProperty("currency_code")
	private String moneda;
	private String email;
	@JsonProperty("source_id")
	private String tokenID;
}

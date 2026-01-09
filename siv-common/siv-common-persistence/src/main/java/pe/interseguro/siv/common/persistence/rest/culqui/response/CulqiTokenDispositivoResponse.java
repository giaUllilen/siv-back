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
public class CulqiTokenDispositivoResponse implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 7616513480721561131L;
	
	@JsonProperty("ip")
	private String ip;
	@JsonProperty("ip_country")
	private String pais;
	@JsonProperty("ip_country_code")
	private String codigoPais;
	@JsonProperty("browser")
	private String navegador;
	@JsonProperty("device_type")
	private String tipo;
}

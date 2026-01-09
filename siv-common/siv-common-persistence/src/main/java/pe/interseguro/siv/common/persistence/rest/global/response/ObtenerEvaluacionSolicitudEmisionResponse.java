package pe.interseguro.siv.common.persistence.rest.global.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObtenerEvaluacionSolicitudEmisionResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("httpStatus")
	private String httpStatus;
	
	@JsonProperty("codigoRespuesta")
	private String codigoRespuesta;
	
	@JsonProperty("mensajeRespuesta")
	private String mensajeRespuesta;
}

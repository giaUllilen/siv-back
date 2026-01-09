package pe.interseguro.siv.common.persistence.rest.crm.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadArchivoResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("IdOportunidad")
	private String idOportunidad;

	private String mensaje;

	@JsonProperty("mensaje_crm")
	private String mensajeCrm;

	private boolean respuesta;
	
	private String message;
}

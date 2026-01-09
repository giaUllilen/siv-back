package pe.interseguro.siv.common.persistence.rest.global.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class DocumentoCorreoResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("codigo")
	private String codigo;
	
	@JsonProperty("mensaje")
	private String mensaje;
	
}

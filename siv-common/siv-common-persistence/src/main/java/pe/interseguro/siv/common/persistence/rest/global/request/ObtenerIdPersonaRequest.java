package pe.interseguro.siv.common.persistence.rest.global.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class ObtenerIdPersonaRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	//@JsonProperty("numero_documento")
	private String numero_documento;
	//@JsonProperty("tipo_documento")
	private String tipo_documento;

}

package pe.interseguro.siv.common.persistence.rest.acsele.request;

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
public class TipoPersona implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -310478356622169770L;

	private String tipo;
	@JsonProperty("DocumentoIdentidad")
	private String documentoIdentidad;
}

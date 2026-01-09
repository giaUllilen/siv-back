package pe.interseguro.siv.common.persistence.rest.global.request;

import java.io.Serializable;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class DocumentoCorreoRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	// @JsonProperty("idSolicitud")
	private Long idSolicitud;
	private String fechaEmision;
	// @JsonProperty("documentos")
	private List<ListDocumentoCorreoRequest> documentos;
}

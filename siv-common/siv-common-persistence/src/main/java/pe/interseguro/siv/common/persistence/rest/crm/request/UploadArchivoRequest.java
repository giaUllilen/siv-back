package pe.interseguro.siv.common.persistence.rest.crm.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadArchivoRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8873032531833434136L;
	
	@JsonProperty("strTipoDoc")
	private String tipoDocumento;
	@JsonProperty("strNroDoc")
	private String numeroDocumento;
	@JsonProperty("strRuta")
	private String rutaArchivo;
	@JsonProperty("strNombreArchivo")
	private String nombreArchivo;
	@JsonProperty("strDescripcion")
	private String descripcionArchivo;
	@JsonProperty("strTipDocAdn")
	private String tipoDocumentoAdn;
	
}

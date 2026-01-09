package pe.interseguro.siv.common.persistence.rest.estudionecesidad.request;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenerarPdfRequest implements Serializable{
	
//	@JsonProperty("producto")
	private String producto;
	
//	@JsonProperty("repositorioCS")
	private String repositorioCS;

//	@JsonProperty("carpetaCS")
	private String carpetaCS;

//	@JsonProperty("solicitud")
	private String solicitud;
	
//	@JsonProperty("numerodocumento")
	private String numerodocumento;
	
//	@JsonProperty("codigoPlantillaEN")
	private String codigoPlantillaEN;
	
//	@JsonProperty("cabeceras")
	private List<GenerarPdfCabeceras> cabeceras;
	
//	@JsonProperty("preguntas")
	private List<GenerarPdfPreguntas> preguntas;
	
//	@JsonProperty("cuadroDetalle")
	private List<String[]> cuadroDetalle;
}

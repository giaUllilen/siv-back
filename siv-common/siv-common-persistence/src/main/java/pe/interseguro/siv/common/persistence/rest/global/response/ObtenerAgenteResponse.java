package pe.interseguro.siv.common.persistence.rest.global.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObtenerAgenteResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("_id")
	private String _id;
	
	@JsonProperty("FUENTE")
	private String FUENTE;
	
	@JsonProperty("CODIGO_GDH")
	private String CODIGO_GDH;
	
	@JsonProperty("COD_JEFE")
	private String COD_JEFE;
	
	@JsonProperty("DOC_JEFE")
	private String DOC_JEFE;
	
	@JsonProperty("NOM_JEFE")
	private String NOM_JEFE;
	
	@JsonProperty("COD_USERNAME_JEFE")
	private String COD_USERNAME_JEFE;
		
	@JsonProperty("GLS_CORREO_JEFE")
	private String GLS_CORREO_JEFE;
	
	@JsonProperty("DOC_SUPERVIS")
	private String DOC_SUPERVIS;
	
	@JsonProperty("NOM_SUPERVIS")
	private String NOM_SUPERVIS;
	
	@JsonProperty("COD_USERNAME_SUPERVIS")
	private String COD_USERNAME_SUPERVIS;
	
	@JsonProperty("GLS_CORREO_SUPERVIS")
	private String GLS_CORREO_SUPERVIS;
		
	@JsonProperty("COD_AGENCIA")
	private String COD_AGENCIA;
	
	@JsonProperty("FLAG_AGENTE_INDICADOR_VIGENCIA")
	private String FLAG_AGENTE_INDICADOR_VIGENCIA;
	
	@JsonProperty("COD_AGENTE")
	private String COD_AGENTE;
	
	@JsonProperty("DOC_AGENTE")
	private String DOC_AGENTE;
	
	@JsonProperty("TIPO_DOCUMENTO_AGENTE")
	private String TIPO_DOCUMENTO_AGENTE;
	
	@JsonProperty("NOM_AGENTE")
	private String NOM_AGENTE;
	
	@JsonProperty("COD_USERNAME_AGENTE")
	private String COD_USERNAME_AGENTE;
	
	@JsonProperty("GLS_CORREO_AGENTE")
	private String GLS_CORREO_AGENTE;
	
	@JsonProperty("CODIGO_CONTRATO")
	private String CODIGO_CONTRATO;
		
	@JsonProperty("CATEGORIA")
	private String CATEGORIA;
	
	@JsonProperty("INICIO_VIGENCIA")
	private String INICIO_VIGENCIA;
	
	@JsonProperty("FECHA_FIN_VIGENCIA")
	private String FECHA_FIN_VIGENCIA;
	
	@JsonProperty("FECHA_INICIO_VIGENCIA")
	private String FECHA_INICIO_VIGENCIA;
	
	@JsonProperty("FIN_VIGENCIA_SUPERVISOR")
	private String FIN_VIGENCIA_SUPERVISOR;
		
	@JsonProperty("INICIO_VIGENCIA_SUPERVISOR")
	private String INICIO_VIGENCIA_SUPERVISOR;
	
	@JsonProperty("NOMBRE_AGENCIA")
	private String NOMBRE_AGENCIA;
	
	@JsonProperty("CODIGO_UBIGEO_AGENCIA")
	private String CODIGO_UBIGEO_AGENCIA;
	
	@JsonProperty("CODIGO_ORIGEN_ZONA")
	private String CODIGO_ORIGEN_ZONA;
	
	@JsonProperty("result")
	private String result;
	
}

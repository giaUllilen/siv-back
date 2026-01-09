package pe.interseguro.siv.common.persistence.rest.global.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import pe.interseguro.siv.common.persistence.rest.niubiz.response.BaseResponse;

@Getter
@Setter
public class ObtenerPersonaDocumentoResponse extends BaseResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("ID_PERSONA")
	private String ID_PERSONA;
	
	@JsonProperty("ID_TIPO_DOCUMENTO")
	private String ID_TIPO_DOCUMENTO;
	
	@JsonProperty("TIPO_DOCUMENTO")
	private String TIPO_DOCUMENTO;
	
	@JsonProperty("NUMERO_DOCUMENTO")
	private String NUMERO_DOCUMENTO;
	
	@JsonProperty("NOMBRE")
	private String NOMBRE;
	
	@JsonProperty("APELLIDO_PATERNO")
	private String APELLIDO_PATERNO;
	
	@JsonProperty("APELLIDO_MATERNO")
	private String APELLIDO_MATERNO;
	
	@JsonProperty("FECHA_NACIMIENTO")
	private String FECHA_NACIMIENTO;
	
	@JsonProperty("httpStatus")
	private String httpStatus;
}

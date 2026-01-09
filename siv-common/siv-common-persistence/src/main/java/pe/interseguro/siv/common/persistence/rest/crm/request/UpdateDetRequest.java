package pe.interseguro.siv.common.persistence.rest.crm.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateDetRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6494138146004231100L;

	@JsonProperty("tipo_documento")
	private String tipoDocumento;

	@JsonProperty("cod_tipo_documento")
	private String codigoTipoDocumento;

	@JsonProperty("numero_documento")
	private String numeroDocumento;

	@JsonProperty("fecha_nacimiento")
	private String fechaNacimiento;

	private String nombres;

	@JsonProperty("apellido_paterno")
	private String apellidoPaterno;

	@JsonProperty("apellido_materno")
	private String apellidoMaterno;

	private String sexo;
	
	@JsonProperty("cod_sexo")
	private String codigoSexo;
	
	@JsonProperty("tel_celular")
	private String telefonoCelular;
	
	private String email;

	private String profesion;

	@JsonProperty("cod_profesion")
	private String codigoProfesion;

	@JsonProperty("actividad_economica")
	private String actividadEconomica;

	@JsonProperty("cod_actividad_economica")
	private String codigoActividadEconomica;

	@JsonProperty("cod_fumador")
	private String codigoFumador;
	
	private String fumador;

	@JsonProperty("DomainName")
	private String domainName;

	
}

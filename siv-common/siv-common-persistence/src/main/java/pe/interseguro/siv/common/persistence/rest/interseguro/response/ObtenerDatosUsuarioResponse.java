package pe.interseguro.siv.common.persistence.rest.interseguro.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import pe.interseguro.siv.common.persistence.rest.niubiz.response.BaseResponse;

@Getter
@Setter
public class ObtenerDatosUsuarioResponse extends BaseResponse{
	@JsonProperty("CodigoEmpleado")
	private String codigoEmpleado;
	
	@JsonProperty("Matricula")
	private String matricula;
	
	@JsonProperty("Nombres")
	private String nombres;

	@JsonProperty("Apellidos")
	private String apellidos;

	@JsonProperty("NombreCompleto")
	private String nombreCompleto;

	@JsonProperty("Dominio")
	private String dominio;
	
	@JsonProperty("Rol")
	private String rol;
	
	@JsonProperty("Estado")
	private String estado;

	@JsonProperty("Correo")
	private String correo;
	
	@JsonProperty("RolAzman")
	private String rolAzman;
}

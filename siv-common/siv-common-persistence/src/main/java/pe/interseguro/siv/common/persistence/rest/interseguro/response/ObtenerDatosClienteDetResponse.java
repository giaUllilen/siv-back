package pe.interseguro.siv.common.persistence.rest.interseguro.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObtenerDatosClienteDetResponse {
	@JsonProperty("idcliente")
	private Integer idCliente;
	private Integer documento;
	@JsonProperty("tipo_documento")
	private String tipoDocumento;
	@JsonProperty("nombre")
	private String nombres;
	@JsonProperty("ape_paterno")
	private String apellidoPaterno;
	@JsonProperty("ape_materno")
	private String apellidoMaterno;
	private String direccion;
	private String email;
	private String celular;
	@JsonProperty("fecha_nac")
	private String fechaNacimiento;
	@JsonProperty("iddepartamento")
	private int idDepartamento;
	private String departamento;
	@JsonProperty("idprovincia")
	private int idProvincia;
	private String provincia;
	@JsonProperty("iddistrito")
	private int idDistrito;
	private String distrito;
}

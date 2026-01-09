package pe.interseguro.siv.common.persistence.rest.interseguro.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ClienteLinkPago implements Serializable {
	private static final long serialVersionUID = 1L;
	private String tipoDocumento;
	private String documentoIdentidad;
	private String nombre;
	private String apellidoPaterno;
	private String apellidoMaterno;
	private String email;
	private String celular;
	private String departamento;
	private String provincia;
	private String distrito;
}

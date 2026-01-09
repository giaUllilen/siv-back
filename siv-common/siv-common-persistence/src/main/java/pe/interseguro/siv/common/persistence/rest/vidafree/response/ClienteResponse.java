package pe.interseguro.siv.common.persistence.rest.vidafree.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4326994720253392845L;

	private String tipoDocumento;
    private String numeroDocumento;
    private String nombre;
    private String fechaNacimiento;
    private String sexo;
    private String sexoDescripcion;
    private String edadContratacion;
    private String condicionFumador;
}

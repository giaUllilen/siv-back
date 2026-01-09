package pe.interseguro.siv.common.persistence.rest.sitc.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AfiliacionPropuestaClienteRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5457884280635607612L;

	private String apellidoPaterno;
	private String apellidoMaterno;
	private String documentoIdentidad;
	private Integer id;
	private String nombres;
	private String tipoDocumento;
}

package pe.interseguro.siv.common.persistence.rest.bupo.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearProspectoRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8143774760668500446L;

	private String tipo_documento;
	private String numero_documento;
	private String nombre;
	private String apellido_paterno;
	private String apellido_materno;
	private String telefono;
	private String celular;
	private String email;
	private String creation_source;
	private String creation_user;
	private String agente_id;
	
}

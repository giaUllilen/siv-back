package pe.interseguro.siv.common.persistence.rest.bupo.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObtenerDatosAgenteResponse implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8143774760668500446L;
	
	private Integer id;
	private String agente_role;
	private String tipo_documento;
	private String numero_documento;
	private String documento;
	private String nombre;
	private String apellido_paterno;
	private String apellido_materno;
	private String telefono;
	private String email;
	private String agencia;
	
}

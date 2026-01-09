package pe.interseguro.siv.common.persistence.rest.vtigger.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsultaUsuarioPorCorreoRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2289090592452266806L;

	private String correoUsuario;
}

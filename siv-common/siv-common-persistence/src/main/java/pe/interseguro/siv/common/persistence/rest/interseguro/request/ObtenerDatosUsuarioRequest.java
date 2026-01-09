package pe.interseguro.siv.common.persistence.rest.interseguro.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObtenerDatosUsuarioRequest {
	private String aplicacion;
	
	private String dominio;

	private String usuario;

	private String contrasena;

}

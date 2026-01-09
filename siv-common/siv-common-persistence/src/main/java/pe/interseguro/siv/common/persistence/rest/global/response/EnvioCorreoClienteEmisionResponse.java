package pe.interseguro.siv.common.persistence.rest.global.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnvioCorreoClienteEmisionResponse  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String codigo;
	private String results;
	
	
}

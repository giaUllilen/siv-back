package pe.interseguro.siv.common.persistence.rest.indenova.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CrearCircuitoLinkSignerResponse implements Serializable {
	
	private String name;
	private String email;
	private String nif;
	

}

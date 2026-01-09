package pe.interseguro.siv.common.persistence.rest.indenova.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearCircuitoLinkResponse implements Serializable {

	private CrearCircuitoLinkSignerResponse signer;
	private String link;
	
	
}

package pe.interseguro.siv.common.persistence.rest.indenova.response;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CrearCircuitoResponse implements Serializable {

	private String circuitId;
	private List<CrearCircuitoLinkResponse> links;
	
	
}

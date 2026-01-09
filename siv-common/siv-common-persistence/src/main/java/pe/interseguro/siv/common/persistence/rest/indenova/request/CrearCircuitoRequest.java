package pe.interseguro.siv.common.persistence.rest.indenova.request;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CrearCircuitoRequest implements Serializable {

	private String include;
	
	private String subject;
	
	private String description;
	
	private List<CrearCircuitoSignerRequest> signers;
	
	private List<CrearCircuitoDocRequest> docs;
	
	private List<CrearCircuitoOptionRequest> options;
	
	
}

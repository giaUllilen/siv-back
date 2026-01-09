package pe.interseguro.siv.common.persistence.rest.indenova.response;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CircuitoResponse implements Serializable {

	private String id;
		
	private String subject;
	
	private String description;
	
	private String status;
	
	private List<CircuitoSingerResponse> signers;
	
	private List<CircuitoDocResponse> docs;
	
	private Object addresees;
	
	private List<CircuitoOptionResponse> options;
	
	
}

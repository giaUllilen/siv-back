package pe.interseguro.siv.common.persistence.rest.indenova.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CircuitoDocResponse implements Serializable {

	private String name;
	private Integer size;
	private String doctype;
	private byte[] content;
	
	
}

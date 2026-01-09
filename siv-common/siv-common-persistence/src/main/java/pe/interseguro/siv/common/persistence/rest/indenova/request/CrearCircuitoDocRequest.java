package pe.interseguro.siv.common.persistence.rest.indenova.request;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class CrearCircuitoDocRequest implements Serializable {

	private String description;
	private String name;
	private Integer size;
	private String doctype;
	private byte[] content;
	
	
}

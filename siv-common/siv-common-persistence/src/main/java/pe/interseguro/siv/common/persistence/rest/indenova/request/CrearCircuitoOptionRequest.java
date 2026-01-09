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
public class CrearCircuitoOptionRequest implements Serializable {

	private String name;
	private String value;
	
	
}

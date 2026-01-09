package pe.interseguro.siv.common.persistence.rest.vtigger.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsultaPotencialRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3087127735560859482L;
	
	private String contactID;
}

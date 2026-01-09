package pe.interseguro.siv.common.persistence.rest.global.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActualizarContactoVtigerRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String cf_852;
	private String cf_854;
	private String cf_928;
	private String cf_1044;
	private String cf_1046; // Tipo de reasinación: ADN, IC
	private String assigned_user_id;
	
}

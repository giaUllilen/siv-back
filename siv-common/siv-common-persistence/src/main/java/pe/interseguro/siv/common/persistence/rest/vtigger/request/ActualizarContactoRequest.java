package pe.interseguro.siv.common.persistence.rest.vtigger.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActualizarContactoRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3288084423388039311L;


	private String id;// formato {12x#####}
	private String cf_852; // Tipo de documento
	private String cf_854; // Numero de documento
	private String firstname;
	private String lastname;
	private String cf_920; // Apellido Materno
	private String phone;
	private String email;
	private String assigned_user_id;
	private String cf_886;  // Valor por defecto Si
	private String cf_860;
	private String cf_907;
	private String cf_905;

}

package pe.interseguro.siv.common.persistence.rest.vtigger.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearContactoRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5896648965321360154L;
	
	private String firstname;
	private String phone;
	private String lastname;
	private String mobile;
	private String leadsource;
	private String assigned_user_id;
	private String email;
	private String cf_852; // Tipo de Documento
	private String cf_854; // Numero de Documento
	private String cf_860; // Fecha de Autorización/Denegación
	private String cf_886; // Autorizado "Si" - "No"
	private String cf_902; // Ubigeo
	private String cf_907; // Hora de autorización
	private String cf_920; // Apellido Materno
	private String cf_905;
	
}

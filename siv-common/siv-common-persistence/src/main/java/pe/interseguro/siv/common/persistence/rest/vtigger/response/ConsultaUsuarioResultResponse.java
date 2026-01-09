package pe.interseguro.siv.common.persistence.rest.vtigger.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsultaUsuarioResultResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1537925873349077457L;

	private String id;
	private String user_name;
	private String first_name;
	private String last_name;
	private String roleid;
	private String rolename;
	private String cf_915;
	private String email1;
}

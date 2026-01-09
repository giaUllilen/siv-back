package pe.interseguro.siv.common.persistence.rest.global.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearContactoVtigerRequest  implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String firstname;
    private String phone;
    private String lastname;
    private String leadsource;
    private String birthday;
    private String email;
    private String assigned_user_id;
    private String source;
    private String cf_852;
    private String cf_854;
    private String cf_860;
    private String cf_886;
    private String cf_920;
    private String cf_1042;
    
}

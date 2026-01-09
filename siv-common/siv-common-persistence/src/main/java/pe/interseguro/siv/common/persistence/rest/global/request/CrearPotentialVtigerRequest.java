package pe.interseguro.siv.common.persistence.rest.global.request;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearPotentialVtigerRequest  implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String tipo_documento;
    private String numero_documento;
    private String potentialname; 
	private String amount;
    private String opportunity_type;
    private String leadsource;
    private String sales_stage;
    private String assigned_user_id;
    private String createdtime;
    private String cf_852;
    private String cf_1050;
    private String cf_864;
    private String cf_870;
    private String cf_872;
    private String cf_876;
	private String cf_878;
    private String cf_880;
}

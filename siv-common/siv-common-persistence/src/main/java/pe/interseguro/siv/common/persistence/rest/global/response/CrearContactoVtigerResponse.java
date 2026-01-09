package pe.interseguro.siv.common.persistence.rest.global.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import pe.interseguro.siv.common.persistence.rest.niubiz.response.BaseResponse;

@Getter
@Setter
public class CrearContactoVtigerResponse extends BaseResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("salutationtype")
	private String salutationtype;
	
	@JsonProperty("firstname")
	private String firstname;
	
	@JsonProperty("contact_no")
	private String contact_no;
	
	@JsonProperty("phone")
	private String phone;
	
	@JsonProperty("lastname")
	private String lastname;
	
	@JsonProperty("mobile")
	private String mobile;
	
	@JsonProperty("account_id")
	private String account_id;
	
	@JsonProperty("homephone")
	private String homephone;
	
	@JsonProperty("leadsource")
	private String leadsource;
	
	@JsonProperty("otherphone")
	private String otherphone;
	
	@JsonProperty("title")
	private String title;
	
	@JsonProperty("fax")
	private String fax;
	
	@JsonProperty("department")
	private String department;
	
	@JsonProperty("birthday")
	private String birthday;
	
	@JsonProperty("email")
	private String email;
	
	@JsonProperty("contact_id")
	private String contact_id;
	
	@JsonProperty("assistant")
	private String assistant;
	
	@JsonProperty("secondaryemail")
	private String secondaryemail;
	
	@JsonProperty("assistantphone")
	private String assistantphone;
	
	@JsonProperty("donotcall")
	private String donotcall;
	
	@JsonProperty("emailoptout")
	private String emailoptout;
	
	@JsonProperty("assigned_user_id")
	private String assigned_user_id;
	
	@JsonProperty("reference")
	private String reference;
	
	@JsonProperty("notify_owner")
	private String notify_owner;
	
	@JsonProperty("createdtime")
	private String createdtime;
	
	@JsonProperty("modifiedtime")
	private String modifiedtime;
	
	@JsonProperty("modifiedby")
	private String modifiedby;
	
	@JsonProperty("otherstreet")
	private String otherstreet;
	
	@JsonProperty("othercity")
	private String othercity;
	
	@JsonProperty("otherstate")
	private String otherstate;
	
	@JsonProperty("otherpobox")
	private String otherpobox;
	
	@JsonProperty("isconvertedfromlead")
	private String isconvertedfromlead;
	
	@JsonProperty("source")
	private String source;
	
	@JsonProperty("starred")
	private String starred;
	
	@JsonProperty("tags")
	private String tags;
	
	@JsonProperty("cf_852")
	private String cf_852;
	
	@JsonProperty("cf_854")
	private String cf_854;
	
	@JsonProperty("cf_858")
	private String cf_858;
	
	@JsonProperty("cf_860")
	private String cf_860;
	
	@JsonProperty("cf_886")
	private String cf_886;
	
	@JsonProperty("cf_902")
	private String cf_902;
	
	@JsonProperty("cf_905")
	private String cf_905;
	
	@JsonProperty("cf_907")
	private String cf_907;
	
	@JsonProperty("cf_920")
	private String cf_920;
	
	@JsonProperty("cf_928")
	private String cf_928;
	
	@JsonProperty("cf_930")
	private String cf_930;
	
	@JsonProperty("cf_1042")
	private String cf_1042;
	
	@JsonProperty("cf_1044")
	private String cf_1044;
	
	@JsonProperty("cf_1046")
	private String cf_1046;
	
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("label")
	private String label;
}

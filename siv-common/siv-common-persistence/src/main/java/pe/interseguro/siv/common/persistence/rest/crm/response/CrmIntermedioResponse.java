package pe.interseguro.siv.common.persistence.rest.crm.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import pe.interseguro.siv.common.persistence.rest.niubiz.response.BaseResponse;

@Getter
@Setter
public class CrmIntermedioResponse extends BaseResponse implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7555148328016608291L;
	
	private Boolean rs;
	private String url;
	private String msg;
}

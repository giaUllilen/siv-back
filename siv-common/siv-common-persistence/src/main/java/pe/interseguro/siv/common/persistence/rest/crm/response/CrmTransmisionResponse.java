package pe.interseguro.siv.common.persistence.rest.crm.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import pe.interseguro.siv.common.persistence.rest.niubiz.response.BaseResponse;

@Getter
@Setter
public class CrmTransmisionResponse extends BaseResponse implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2734061792443934206L;
	
	private String errorCode;
	private String errorMessage;
	private String respuesta;
	private String estadoOperacion;
	private String estadoPoliza;
	private String idOperacion;
	private String idPoliza;
}

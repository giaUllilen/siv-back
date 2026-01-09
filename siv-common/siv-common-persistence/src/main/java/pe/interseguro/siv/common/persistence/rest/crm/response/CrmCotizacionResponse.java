package pe.interseguro.siv.common.persistence.rest.crm.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import pe.interseguro.siv.common.persistence.rest.niubiz.response.BaseResponse;
@Getter
@Setter
public class CrmCotizacionResponse extends BaseResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4941887028943785544L;
	
	private String idCotizacionCRM;
	private Boolean rs;
	private String msg;
}

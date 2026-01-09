package pe.interseguro.siv.common.persistence.rest.crm.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import pe.interseguro.siv.common.persistence.rest.niubiz.response.BaseResponse;

@Getter
@Setter
public class GetEstadoCotizacionResponse extends BaseResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7263277261326700719L;

	private String strCodigo;
	
	private String strDescripcion;
}

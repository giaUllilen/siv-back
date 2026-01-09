package pe.interseguro.siv.common.persistence.rest.vtigger.response;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import pe.interseguro.siv.common.persistence.rest.niubiz.response.BaseResponse;

@Getter
@Setter
public class ConsultaClienteResponse extends BaseResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3651362851657415993L;
	
	private boolean success;
	private List<ConsultaClienteResultResponse> result;
}

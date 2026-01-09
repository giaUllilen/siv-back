package pe.interseguro.siv.common.persistence.rest.vtigger.response;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import pe.interseguro.siv.common.persistence.rest.niubiz.response.BaseResponse;

@Getter
@Setter
public class ConsultaUsuarioPorCorreoResponse extends BaseResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5105084425351767710L;

	private boolean success;
	private List<ConsultaUsuarioPorCorreoResultResponse> result;	
}

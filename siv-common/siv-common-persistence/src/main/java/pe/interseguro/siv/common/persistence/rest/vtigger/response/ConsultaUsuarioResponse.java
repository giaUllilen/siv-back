package pe.interseguro.siv.common.persistence.rest.vtigger.response;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import pe.interseguro.siv.common.persistence.rest.niubiz.response.BaseResponse;

@Getter
@Setter
public class ConsultaUsuarioResponse extends BaseResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1611257129432526686L;

	private boolean success;
	private List<ConsultaUsuarioResultResponse> result;	
}

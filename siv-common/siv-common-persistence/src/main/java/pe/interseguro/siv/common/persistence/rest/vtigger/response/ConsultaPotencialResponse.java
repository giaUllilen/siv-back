package pe.interseguro.siv.common.persistence.rest.vtigger.response;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import pe.interseguro.siv.common.persistence.rest.niubiz.response.BaseResponse;

@Getter
@Setter
public class ConsultaPotencialResponse extends BaseResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2420625671793099989L;

	private boolean success;
	private List<ConsultaPotencialResultResponse> result;
}

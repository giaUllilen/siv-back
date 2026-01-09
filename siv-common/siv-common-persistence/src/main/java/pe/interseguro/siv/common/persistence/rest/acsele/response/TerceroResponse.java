package pe.interseguro.siv.common.persistence.rest.acsele.response;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import pe.interseguro.siv.common.persistence.rest.niubiz.response.BaseResponse;

@Getter
@Setter
public class TerceroResponse extends BaseResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1559013526312045789L;
	
	@JsonProperty("Terceros")
	private List<Tercero> terceros;
	@JsonProperty("Errores")
	private List<Error> errores;
}

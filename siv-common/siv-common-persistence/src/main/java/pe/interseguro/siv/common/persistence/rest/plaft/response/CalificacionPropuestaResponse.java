package pe.interseguro.siv.common.persistence.rest.plaft.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import pe.interseguro.siv.common.persistence.rest.interseguro.request.DigitalTokenDetResponse;
import pe.interseguro.siv.common.persistence.rest.niubiz.response.BaseResponse;

@Getter
@Setter
public class CalificacionPropuestaResponse extends BaseResponse implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -776885437640905225L;

	@JsonProperty("_meta")
	private CalificacionPropuestaMetaResponse meta;
	
	@JsonProperty("records")
	private CalificacionPropuestaDetResponse records;
}

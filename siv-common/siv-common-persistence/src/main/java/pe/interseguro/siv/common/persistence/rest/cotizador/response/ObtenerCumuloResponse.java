package pe.interseguro.siv.common.persistence.rest.cotizador.response;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import pe.interseguro.siv.common.persistence.rest.niubiz.response.BaseResponse;

@Getter
@Setter
public class ObtenerCumuloResponse extends BaseResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3870009068479274852L;
	
	private String err;
	private String id;
	private String msg;
	private String rs;
	private String idTercero;
	@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
	private List<MontoDetalleResponse> montosdet;
}

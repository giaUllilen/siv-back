package pe.interseguro.siv.common.persistence.rest.interseguro.response;

import lombok.Getter;
import lombok.Setter;
import pe.interseguro.siv.common.persistence.rest.niubiz.response.BaseResponse;

@Getter
@Setter
public class ObtenerDatosClienteResponse extends BaseResponse {
	private Boolean success;
	private String mensaje;
	private ObtenerDatosClienteDetResponse data;
}

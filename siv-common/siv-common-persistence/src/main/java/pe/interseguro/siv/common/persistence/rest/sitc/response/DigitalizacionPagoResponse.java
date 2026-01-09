package pe.interseguro.siv.common.persistence.rest.sitc.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DigitalizacionPagoResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1428327341103475668L;

	private String codigoRespuesta;
	private String mensajeRespuesta;
	private String idDigitalizacionPagosVida;
}

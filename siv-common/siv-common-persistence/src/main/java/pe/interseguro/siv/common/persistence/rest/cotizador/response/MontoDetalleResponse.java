package pe.interseguro.siv.common.persistence.rest.cotizador.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MontoDetalleResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6806222650813423310L;
	
	private String idmoneda;
	private String monto;

}

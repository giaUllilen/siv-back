package pe.interseguro.siv.common.persistence.rest.vidafree.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsultaCotizacionDetalleRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4701430274178499165L;
	
	private String idAgente;
	private String numeroCotizacion;
}

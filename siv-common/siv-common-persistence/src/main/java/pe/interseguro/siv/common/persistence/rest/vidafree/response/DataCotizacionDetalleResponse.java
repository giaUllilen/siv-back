package pe.interseguro.siv.common.persistence.rest.vidafree.response;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import pe.interseguro.siv.common.bean.DocumentoCotizacionDevolucion;

@Getter
@Setter
public class DataCotizacionDetalleResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8699588765097891606L;
	
	private AgenteResponse agente;
	private ClienteResponse cliente;
	private CotizacionDetalleResponse detalleCotizacion;
}

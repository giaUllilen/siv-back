package pe.interseguro.siv.common.persistence.rest.crm.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrmActualizarSitcRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3786988745568271729L;

	private String idCotizacion;
	private String strNumeroPropuesta;
}

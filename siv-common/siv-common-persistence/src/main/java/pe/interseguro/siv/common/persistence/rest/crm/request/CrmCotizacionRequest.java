package pe.interseguro.siv.common.persistence.rest.crm.request;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrmCotizacionRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7333182793444803245L;
	
	private CrmCotizacionBERequest beCotizacion;

}

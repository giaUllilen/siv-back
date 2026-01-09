package pe.interseguro.siv.common.persistence.rest.crm.request;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrmOportunidadCotizacionRequest implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4289812762098890073L;
	private String userId;
	private String oportunidadId;
	private String jsonCotizaciones;
}

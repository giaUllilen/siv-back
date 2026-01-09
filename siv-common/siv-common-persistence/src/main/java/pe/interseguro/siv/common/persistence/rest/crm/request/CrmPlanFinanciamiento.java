package pe.interseguro.siv.common.persistence.rest.crm.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrmPlanFinanciamiento implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1269711165521937265L;
	
	private String idPFConf;
    private String nombrePlanFinanciamiento;
    private String moneda;
}

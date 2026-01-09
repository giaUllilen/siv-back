package pe.interseguro.siv.common.persistence.rest.crm.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrmCobertura implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8995484411991276757L;
	
	private String idCovConf;
    private String covBeneficioMaximo;
    private String nombreCov;
}

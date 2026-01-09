package pe.interseguro.siv.common.dto.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CotizadorCrmCoberturaDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4963367473690562775L;
	
	private String idCovConf;
    private String covBeneficioMaximo;
    private String nombreCov;

}

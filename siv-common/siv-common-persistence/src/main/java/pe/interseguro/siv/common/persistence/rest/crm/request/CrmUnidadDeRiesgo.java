package pe.interseguro.siv.common.persistence.rest.crm.request;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrmUnidadDeRiesgo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4532435231634437588L;
	
	private String idURConf;
    private String nombreUR;
    private String numUnidRiesgo;
    private String productosVida;
    private String planVida;
    private String opcionVida;
    private String capitalAsegurado;
    private String periodoPagoPrima;
    private String periodoPagoPrimaVE;
    private String numeroAnualidades;
    private String vigenciaEG;
    private List<CrmObjetoAsegurado> objetosAsegurados;
}

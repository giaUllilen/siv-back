package pe.interseguro.siv.common.dto.request;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CotizadorCrmUnidadRiesgo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4619609535608196871L;
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
    private List<CotizadorCrmObjetoAseguradoDTO> objetosAsegurados;
}

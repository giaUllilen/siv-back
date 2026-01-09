package pe.interseguro.siv.common.persistence.rest.vidafree.request;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CotizacionCoberturaRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2881406842660517060L;

	private String codCobertura;
    private String sumaAsegurada;
    private Integer periodoVigencia;
    private Integer periodoPago;
    private Double primaRecurrente;
}

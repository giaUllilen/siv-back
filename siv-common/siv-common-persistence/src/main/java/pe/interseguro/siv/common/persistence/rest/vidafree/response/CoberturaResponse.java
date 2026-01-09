package pe.interseguro.siv.common.persistence.rest.vidafree.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoberturaResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8435504345375724614L;
	
	private String codCobertura;
	private String codCoberturaAcsele;
    private String nombreCobertura;
    private String nombreCoberturaReporte;
    private Integer periodoVigencia;
    private Integer periodoPago;
    private Double sumaAsegurada;
    private Double primaRecurrente;
    private Double tasaRecurrente;
    private Double tasaAnual;
    private Double primaClienteAnual;
    private Integer porcentajeDevolucion;
    private Double montoDevolucion;
    private String edadMinimaIngreso;
    private String edadMaximaIngreso;
    private String edadMaximaPermanencia;

}

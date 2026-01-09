package pe.interseguro.siv.common.persistence.rest.vidafree.request;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CotizacionRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7372480442361750848L;

	private String numeroCotizacion;
    private String codProducto;
    private Integer codPlan;
    private Integer codMoneda;
    private Integer porcentajeDevolucion;
    private String frecuenciaPago;
    private String sexo;
    private Integer edadContratacion;
    private String fechaNacimiento;
    private String condicionFumador;
    private Boolean inversa;
    private Double primaInversaTotal;
    private List<CotizacionCoberturaRequest> coberturas;
    private String cumuloMoneda;
    private Integer cumuloMonto;
    private Double currentCumuloMonto;
}

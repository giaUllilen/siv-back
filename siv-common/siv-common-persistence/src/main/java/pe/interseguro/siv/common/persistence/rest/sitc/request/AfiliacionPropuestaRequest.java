package pe.interseguro.siv.common.persistence.rest.sitc.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AfiliacionPropuestaRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -631875684405371715L;

	private AfiliacionPropuestaClienteRequest cliente;
	private String usuario;
	private String usuarioAplicacion;
	private String fechaIngresoMandato;
	private String token;
	private Integer id;
	private String numeroCotizacion;
	private String indAfiliacion;
	private String moneda;
	private Integer numeroDiaCobro;
	private Integer numeroProcesoAfiliacion;
	private Long numeroPropuesta;
	private String periodoPago;
	private String ramo;
	private String subramo;
	private AfiliacionPropuestaPagoPrimeroRequest pagoPrimero;
	private AfiliacionPropuestaPagoRecurrenteRequest pagoRecurrente;
	private String telefonoTitular;
	private String primaFormaPago;
}

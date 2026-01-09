package pe.interseguro.siv.common.persistence.rest.vidafree.response;

import java.io.Serializable;
import java.util.Date;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class CotizacionResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6518803351927566655L;
	
	private String numeroCotizacion;
	private String fechaCotizacion;
	private String nombreProducto;
	private String nombrePlan;
	private String frecuencia;
	private String primaRecurrente;
	private String primaAnual;
	private String moneda;
	private String numeroCotizacionOrigen;
	private String estado;
	private Date fechaRegistroSamp;
	private String estadoGeneral;
}

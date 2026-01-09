package pe.interseguro.siv.common.persistence.rest.sitc.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AfiliacionPropuestaPagoPrimeroRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7442861954760548795L;

	private String estado;
	private String fechaVencimiento;
	private String id;
	private String idAfiliacion;
	private String moneda;
	private String numeroDiaCobro;
	private String numeroPoliza;
	private String tipoCuenta;
	private String tipoViaCobro;
	private String viaCobro;
	private String numeroCuentaTarjeta;
}

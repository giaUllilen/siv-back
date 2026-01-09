package pe.interseguro.siv.common.persistence.rest.sitc.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AfiliacionPropuestaPagoRecurrenteRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3703060465337066073L;
	
	private Integer id;
	private String moneda;
	private String numeroTarjetaCuenta;
	private String tipoCuenta;
	private String tipoViaCobro;
	private String viaCobro;
	private Integer diaCobro;
	private Long numeroPoliza;

}

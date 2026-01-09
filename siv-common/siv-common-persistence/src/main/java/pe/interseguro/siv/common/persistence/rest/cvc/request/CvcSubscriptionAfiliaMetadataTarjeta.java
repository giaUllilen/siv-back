package pe.interseguro.siv.common.persistence.rest.cvc.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CvcSubscriptionAfiliaMetadataTarjeta {
	private String fechavencimientocadena;
	private String moneda;
	private String numerotarjeta;
	private String numerocuenta;
	private String tipocuenta;
	private String tipoviacobro;
	private String viacobro;
}

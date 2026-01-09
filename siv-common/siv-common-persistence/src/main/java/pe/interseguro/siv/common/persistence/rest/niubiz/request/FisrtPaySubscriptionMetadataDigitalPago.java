package pe.interseguro.siv.common.persistence.rest.niubiz.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FisrtPaySubscriptionMetadataDigitalPago {
	private String numeropropuesta;
	private String numeropoliza;
	private String idoperacion;
	private String moneda;
	private String monto;
	private String fechapago;
	private String tipopago;
	private String codusuariocreador;
	private String numip;
}

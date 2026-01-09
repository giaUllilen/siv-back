package pe.interseguro.siv.common.persistence.rest.cvc.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CvcSubscriptionAfiliaMetadataAfiliacion {
	private String fechaingresomandato;
	private String moneda;
	private String numeropropuesta;
	private String numeropoliza;
	private String periodopago;
	private String tipooperacion;
	private String codusuariocreador;
	private String fechaOperacion;
	private String datoadicional;
}

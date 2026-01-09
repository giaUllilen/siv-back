package pe.interseguro.siv.common.persistence.rest.niubiz.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FisrtPaySubscriptionMetadataCliente {
	private String apellidopaterno;
	private String apellidomaterno;
	private String documentoidentidad;
	private String nombre;
	private String tipodocumento;
}

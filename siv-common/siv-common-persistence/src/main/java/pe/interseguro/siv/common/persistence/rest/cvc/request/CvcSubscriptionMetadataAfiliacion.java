package pe.interseguro.siv.common.persistence.rest.cvc.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CvcSubscriptionMetadataAfiliacion {
	private CvcSubscriptionAfiliaMetadataCliente cliente;
	private CvcSubscriptionAfiliaMetadataAfiliacion afiliacion;
	private CvcSubscriptionAfiliaMetadataTarjeta tarjeta;
	private CvcSubscriptionAfiliaMetadataTarjetaPp tarjeta_pp;
}

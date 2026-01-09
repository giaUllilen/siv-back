package pe.interseguro.siv.common.persistence.rest.interseguro.request;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class LinkPagoRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	private String identificador;
	private String producto;
	private String productoAcsele;
	private int expiracion;
	private String codigoEnvio;
	private ClienteLinkPago cliente;
	private PagoLinkPago pago;
	private String aplicacion;
	private boolean esRecargo;
	private byte estadoPropuesta;
	private boolean primeraPrima;
	private boolean bloquearEnvio;
}
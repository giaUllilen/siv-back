package pe.interseguro.siv.common.persistence.rest.interseguro.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class PagoLinkPago implements Serializable {
	private static final long serialVersionUID = 1L;
	private String moneda;
	private String monedaSimbolo;
	private String monto;
	private String frecuencia;
	private long[] cuotas;
}

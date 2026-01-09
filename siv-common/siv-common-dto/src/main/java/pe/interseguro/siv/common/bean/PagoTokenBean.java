package pe.interseguro.siv.common.bean;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class PagoTokenBean {
	private String solicitud;
	private String documento;
	private String cliente;
	private String correoAgente;
	private String numeroPropuesta;
	private String plan;
	private String monto;
	private String frecuencia;
	private String time;
	private String moneda;
	private String montoValue;
	private String correoCliente;
	private String afiliacion;
}

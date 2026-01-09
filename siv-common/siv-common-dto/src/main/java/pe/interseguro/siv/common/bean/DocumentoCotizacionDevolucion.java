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
public class DocumentoCotizacionDevolucion {
	private String anio;
	private String devEstandar;
	private String devCliente;
	private String devolucion;
}

package pe.interseguro.siv.common.bean;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class DocumentoCotizacionCobertura implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1508310444778264141L;
	private String nombre;
	private String capital;
	private String tasa;
	private String edadMinimaIngreso;
	private String edadMaximoIngreso;
	private String edadMaximaPerm;
	private String vigenciaCobertura;
	private String primaAnual;
}

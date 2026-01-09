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
public class SolicitudPDFProductoDetalleBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2555961116236512306L;
	
	private String tipoCobetura;
	private String cobertura;
	private String capitalAsegurado;
	private String primaAnual;

}

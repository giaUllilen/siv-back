package pe.interseguro.siv.common.dto.response;

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
public class ADNPolizaResponseDTO implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1536199299959393360L;
	
	private String numeroPoliza;
	private String estadoPoliza;
	private String nombreProducto;
	private String frecuenciaPago;
	private String primaTotal;
	private String inicioVigencia;
	private String finVigencia;
	private String cobertura;
	private String capital;
	private String primaNeta;
	private String ultimoPago;
}

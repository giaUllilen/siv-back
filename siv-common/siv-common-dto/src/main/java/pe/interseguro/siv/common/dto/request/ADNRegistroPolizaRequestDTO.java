package pe.interseguro.siv.common.dto.request;

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
public class ADNRegistroPolizaRequestDTO implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String tipoDocumento;
	private String numeroDocumento;
	private String numeroPoliza;
	private String fechaEmision;
	private String nombreProducto;
	private String estadoPoliza;
	private String pagadoHasta;
	private String moneda;
	private String montoCobertura;
	private String vidaAdicional;

}

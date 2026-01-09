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
public class ADNPolizaBUCResponseDTO implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1536199299959393360L;
	
	private String numeroPoliza;
	private String fechaEmision;
	private String nombreProducto;		
	private String estadoPoliza;
	private String pagadoHasta;
	private String moneda;
	private String montoCoberturaPrincipal;
	private String coberturaVidaAdicional;
}

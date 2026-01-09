package pe.interseguro.siv.common.dto.response;

import java.io.Serializable;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class SolicitudFormularioCotizacionResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3816275459979307707L;

	private String producto;
	private String plazoCobertura;
	private String montoPrima;
	private String frecuenciaPago;
	private String fondoGarantizado;
	private String tasaGarantizada;
	private String pagoBeneficios;
	private String numeroCotizacion;
	private List<SolicitudFormularioCoberturaResponseDTO> coberturas;
}

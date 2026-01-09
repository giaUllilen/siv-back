package pe.interseguro.siv.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CotizadorCorrelativoResponseDTO extends BaseResponseDTO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7321471573925705271L;
	
	private String numeroCotizacion;
	private String cumuloMoneda;
	private String cumuloValor;
}

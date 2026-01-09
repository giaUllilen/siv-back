package pe.interseguro.siv.common.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ConversionResponseDTO extends BaseResponseDTO{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6971639517956205965L;
	private String monedaOrigen;
	private String monedaConversion;
	private String fecha;
	private Double valor;
}

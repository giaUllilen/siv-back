package pe.interseguro.siv.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CotizadorCumuloResponseDTO extends BaseResponseDTO {/**
	 * 
	 */
	private static final long serialVersionUID = 7922336910003331835L;

	private String idTercero;
	private Double cumuloSoles;
	private Double cumuloDolares;
}

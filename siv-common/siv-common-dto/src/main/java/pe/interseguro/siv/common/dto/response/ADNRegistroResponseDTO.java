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
public class ADNRegistroResponseDTO extends BaseResponseDTO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7112288323998252582L;
	
	private Integer idAdn;
	private String idContactoCRM;
	private String idOportunidadCRM;
	private String urlCotizadorVida;
	private String urlCotizadorFlex;
	private String urlCotizadorVidaFree;
	private Boolean requiredUpdateConsentimiento;
	
}

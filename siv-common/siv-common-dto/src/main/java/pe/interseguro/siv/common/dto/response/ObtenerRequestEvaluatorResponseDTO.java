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
public class ObtenerRequestEvaluatorResponseDTO extends BaseResponseDTO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5691713304951442846L;
	private String estadoPropuesta;
	private String numeroPropuesta;

}

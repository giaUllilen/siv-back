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
public class ADNAutoguardadoResponseDTO extends BaseResponseDTO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9088733668177506547L;
	
	private String idAutoguardado;
}

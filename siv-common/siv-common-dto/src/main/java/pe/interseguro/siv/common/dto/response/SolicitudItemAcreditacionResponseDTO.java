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
public class SolicitudItemAcreditacionResponseDTO extends BaseResponseDTO {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String numCot;
	private String rpta;

}

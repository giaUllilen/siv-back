package pe.interseguro.siv.common.dto.response;

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
public class ADNConsultaPolizaBUCResponseDTO extends BaseResponseDTO {/**
	 * 
	 */
	private static final long serialVersionUID = -3754036097533259327L;

	private List<ADNPolizaBUCResponseDTO> polizas;
}

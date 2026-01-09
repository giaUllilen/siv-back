package pe.interseguro.siv.common.dto.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListObtenerIdPersonaResponseDTO extends BaseResponseDTO {

	
		private static final long serialVersionUID = 5699070480716869016L;
		
		private List<ObtenerPersonaDocumentoResponseDTO> data;

	
}

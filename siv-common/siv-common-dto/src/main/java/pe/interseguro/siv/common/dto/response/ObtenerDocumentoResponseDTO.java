package pe.interseguro.siv.common.dto.response;

import java.io.Serializable;
import java.util.Date;
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
public class ObtenerDocumentoResponseDTO extends BaseResponseDTO {/**
	 * 
	 */
	private static final long serialVersionUID = 8208177355211315621L;

	private String[] archivos;
//	private List<ObtenerDocumentoItemResponseDTO> archivos;
}

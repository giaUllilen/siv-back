package pe.interseguro.siv.common.dto.response;

import java.io.Serializable;
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
public class CRMReporteRrvvResponseDTO implements Serializable { /*extends BaseResponseDTO {/**
	 * 
	 */
	private static final long serialVersionUID = -6908088866583493722L;
	
	private List<CRMReporteDetalleRrvvResponseDTO> registros;
}

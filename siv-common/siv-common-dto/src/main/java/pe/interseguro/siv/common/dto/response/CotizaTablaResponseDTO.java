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
public class CotizaTablaResponseDTO extends BaseResponseDTO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7392470339007516089L;
	
	private String codigo;
	private String nombre;
	private List<CotizaTablaItemResponseDTO> lista;

	
}

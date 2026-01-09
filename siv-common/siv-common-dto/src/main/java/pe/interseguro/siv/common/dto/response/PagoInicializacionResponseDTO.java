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
public class PagoInicializacionResponseDTO extends BaseResponseDTO {/**
	 * 
	 */
	private static final long serialVersionUID = -9030816868252132298L;

	private List<ADNInicializacionViaCobroResponseDTO> viasCobro;
}

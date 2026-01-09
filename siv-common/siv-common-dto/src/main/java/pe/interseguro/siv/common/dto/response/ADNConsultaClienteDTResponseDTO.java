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
public class ADNConsultaClienteDTResponseDTO extends BaseResponseDTO {/**
	 * 
	 */
	private static final long serialVersionUID = 7428454051035725146L;
	
	private List<ADNClienteDTResponseDTO> result;
}

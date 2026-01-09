package pe.interseguro.siv.common.dto.response;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor; 
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.interseguro.siv.common.bean.GenericoComboBean;
import pe.interseguro.siv.common.exception.ErrorResourceDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ADNCodigoTablaResponseDTO extends BaseResponseDTO {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 6232820797336432498L;
	private List<GenericoComboBean> lista;	
}

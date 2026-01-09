package pe.interseguro.siv.common.dto.request;

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
public class ADNRegistroReferidoRequestDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 715466335149145429L;
	
	private Long idAdn;
	private List<ADNReferidoRequestDTO> referidos;
	private String idUsuario;
}

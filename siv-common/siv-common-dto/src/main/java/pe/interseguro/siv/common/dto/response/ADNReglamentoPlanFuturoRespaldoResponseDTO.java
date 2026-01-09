package pe.interseguro.siv.common.dto.response;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ADNReglamentoPlanFuturoRespaldoResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4516724720084674130L;

	private String ahorro;
	private Integer ahorroCheck;
	private String propiedad;
	private Integer propiedadCheck;
	private String vehiculo;
	private Integer vehiculoCheck;
	private String seguroVida;
	private Integer seguroVidaCheck;
	private String seguroVidaLey;
	private Integer seguroVidaLeyCheck;
	
}

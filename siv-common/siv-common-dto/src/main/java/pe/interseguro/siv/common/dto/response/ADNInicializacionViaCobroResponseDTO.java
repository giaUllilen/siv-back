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
public class ADNInicializacionViaCobroResponseDTO implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = -3553219201100367758L;

	private Integer tipoViaCobro;
	private Integer codigoViaCobro;
	private String nombreViaCobro;
	private String tipoCuenta;
	private String mascara;
	private Integer longitud;
	private Integer ccv;
}

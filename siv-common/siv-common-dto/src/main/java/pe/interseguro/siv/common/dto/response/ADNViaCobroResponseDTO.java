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
public class ADNViaCobroResponseDTO implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 6204314648423428454L;

	private Integer tipoViaCobro;
	private String nombreViaCobro;
	private String tipoCuenta;
	private String mascara;
	private Integer longitud;;
}

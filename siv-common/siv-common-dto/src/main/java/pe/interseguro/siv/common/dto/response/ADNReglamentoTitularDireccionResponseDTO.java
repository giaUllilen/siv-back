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
public class ADNReglamentoTitularDireccionResponseDTO implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1351880203670158455L;

	private String tipoVia;
	private String nombreVia;
	private String numeroVia;
	private String dptoNro;
	private String urbanizacion;
	private String departamento;
	private String provincia;
	private String distrito;
}

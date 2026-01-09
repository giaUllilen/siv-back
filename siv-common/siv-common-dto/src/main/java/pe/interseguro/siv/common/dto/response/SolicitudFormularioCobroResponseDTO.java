package pe.interseguro.siv.common.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class SolicitudFormularioCobroResponseDTO extends BaseResponseDTO {/**
	 * 
	 */
	private static final long serialVersionUID = -6532875130998168675L;

	private String tipoViaCobro;
	private String viaCobro;
	private String moneda;
	private String tipoCuenta;
	private String numeroCuentaTarjeta;
	private String fechaVencimiento;
}

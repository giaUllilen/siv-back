package pe.interseguro.siv.common.dto.request;

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
public class SolicitudPagoTarjetaRequestDTO extends BaseRequestDTO {/**
	 * 
	 */
	private static final long serialVersionUID = -8883477051265719176L;

	private String numeroTarjeta;
	private String ccv;
	private String email;
	private Integer mesVencimiento;
	private Integer anioVencimiento;
}

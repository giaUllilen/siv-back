package pe.interseguro.siv.common.dto.request;

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
public class ADNRegistroPlanFuturoCapitalRequestDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1648130813377203899L;

	private Double capitalProteccion;
	
	private Double respaldoEconomico;
	
	private Double afp;
	
	private Integer afpCheck;
	
	private Double totalCapitalProteger;
	
	private Double adicional;
	
	private Double nuevoCapitalProteger;
}

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
public class ADNRegistroPlanFuturoRespaldoRequestDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2347993927011889988L;

	private Double ahorro;

	private Integer ahorroCheck;
	
	private Double propiedad;
	
	private Integer propiedadCheck;
	
	private Double vehiculo;
	
	private Integer vehiculoCheck;
	
	private Double seguroVida;
	
	private Integer seguroVidaCheck;
	
	private Double seguroVidaLey;
	
	private Integer seguroVidaLeyCheck;

}

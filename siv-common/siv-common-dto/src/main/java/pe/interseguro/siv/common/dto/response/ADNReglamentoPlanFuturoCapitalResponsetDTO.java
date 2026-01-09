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
public class ADNReglamentoPlanFuturoCapitalResponsetDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8231978142346796927L;

	private String capitalProteccion;
	private String respaldoEconomico;
	private Integer afpCheck;
	private String afp;
	private String totalCapitalProteger;
	private String adicional;
	private String nuevoCapitalProteger;

	
}

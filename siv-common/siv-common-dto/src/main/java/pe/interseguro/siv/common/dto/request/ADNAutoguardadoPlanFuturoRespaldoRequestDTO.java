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
public class ADNAutoguardadoPlanFuturoRespaldoRequestDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 63283250973444069L;

	private String ahorro;
	private String propiedad;
	private String vehiculo;
	private String seguroVida;
	private String seguroVidaLey;
	public String getAhorro() {
		return ahorro;
	}
	public void setAhorro(String ahorro) {
		this.ahorro = ahorro;
	}
	public String getPropiedad() {
		return propiedad;
	}
	public void setPropiedad(String propiedad) {
		this.propiedad = propiedad;
	}
	public String getVehiculo() {
		return vehiculo;
	}
	public void setVehiculo(String vehiculo) {
		this.vehiculo = vehiculo;
	}
	public String getSeguroVida() {
		return seguroVida;
	}
	public void setSeguroVida(String seguroVida) {
		this.seguroVida = seguroVida;
	}
	public String getSeguroVidaLey() {
		return seguroVidaLey;
	}
	public void setSeguroVidaLey(String seguroVidaLey) {
		this.seguroVidaLey = seguroVidaLey;
	}
	
	
}

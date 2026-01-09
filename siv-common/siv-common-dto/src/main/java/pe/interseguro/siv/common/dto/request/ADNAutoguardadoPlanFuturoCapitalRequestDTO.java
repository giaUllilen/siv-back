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
public class ADNAutoguardadoPlanFuturoCapitalRequestDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3780898999770726933L;

	private String capitalProteccion;
	private String respaldoEconomico;
	private String afp;
	private String totalCapitalProteger;
	private String adicional;
	private String nuevoCapitalProteger;
	public String getCapitalProteccion() {
		return capitalProteccion;
	}
	public void setCapitalProteccion(String capitalProteccion) {
		this.capitalProteccion = capitalProteccion;
	}
	public String getRespaldoEconomico() {
		return respaldoEconomico;
	}
	public void setRespaldoEconomico(String respaldoEconomico) {
		this.respaldoEconomico = respaldoEconomico;
	}
	public String getAfp() {
		return afp;
	}
	public void setAfp(String afp) {
		this.afp = afp;
	}
	public String getTotalCapitalProteger() {
		return totalCapitalProteger;
	}
	public void setTotalCapitalProteger(String totalCapitalProteger) {
		this.totalCapitalProteger = totalCapitalProteger;
	}
	public String getAdicional() {
		return adicional;
	}
	public void setAdicional(String adicional) {
		this.adicional = adicional;
	}
	public String getNuevoCapitalProteger() {
		return nuevoCapitalProteger;
	}
	public void setNuevoCapitalProteger(String nuevoCapitalProteger) {
		this.nuevoCapitalProteger = nuevoCapitalProteger;
	}

	
}

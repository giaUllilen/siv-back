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
public class ADNReglamentoPlanFuturoResponsetDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5053110910580243979L;

	private ADNReglamentoPlanFuturoBaseResponseDTO base;
	private ADNReglamentoPlanFuturoRespaldoResponseDTO respaldo;
	private ADNReglamentoPlanFuturoCapitalResponsetDTO capitalProteger;
	public ADNReglamentoPlanFuturoBaseResponseDTO getBase() {
		return base;
	}
	public void setBase(ADNReglamentoPlanFuturoBaseResponseDTO base) {
		this.base = base;
	}
	public ADNReglamentoPlanFuturoRespaldoResponseDTO getRespaldo() {
		return respaldo;
	}
	public void setRespaldo(ADNReglamentoPlanFuturoRespaldoResponseDTO respaldo) {
		this.respaldo = respaldo;
	}
	public ADNReglamentoPlanFuturoCapitalResponsetDTO getCapitalProteger() {
		return capitalProteger;
	}
	public void setCapitalProteger(ADNReglamentoPlanFuturoCapitalResponsetDTO capitalProteger) {
		this.capitalProteger = capitalProteger;
	}
	
	
}

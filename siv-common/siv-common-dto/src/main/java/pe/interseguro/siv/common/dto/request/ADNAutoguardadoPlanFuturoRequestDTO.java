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
public class ADNAutoguardadoPlanFuturoRequestDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -273865929849245159L;
	
	private ADNAutoguardadoPlanFuturoBaseRequestDTO base;
	private ADNAutoguardadoPlanFuturoRespaldoRequestDTO respaldo;
	private ADNAutoguardadoPlanFuturoCapitalRequestDTO capitalProteger;
	public ADNAutoguardadoPlanFuturoBaseRequestDTO getBase() {
		return base;
	}
	public void setBase(ADNAutoguardadoPlanFuturoBaseRequestDTO base) {
		this.base = base;
	}
	public ADNAutoguardadoPlanFuturoRespaldoRequestDTO getRespaldo() {
		return respaldo;
	}
	public void setRespaldo(ADNAutoguardadoPlanFuturoRespaldoRequestDTO respaldo) {
		this.respaldo = respaldo;
	}
	public ADNAutoguardadoPlanFuturoCapitalRequestDTO getCapitalProteger() {
		return capitalProteger;
	}
	public void setCapitalProteger(ADNAutoguardadoPlanFuturoCapitalRequestDTO capitalProteger) {
		this.capitalProteger = capitalProteger;
	}
	
	
}

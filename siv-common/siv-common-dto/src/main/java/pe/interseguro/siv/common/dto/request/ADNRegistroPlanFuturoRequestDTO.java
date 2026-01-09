package pe.interseguro.siv.common.dto.request;

import java.io.Serializable;

import javax.validation.Valid;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ADNRegistroPlanFuturoRequestDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1831958562732244357L;
	
	@Valid
	private ADNRegistroPlanFuturoBaseRequestDTO base;
	
	@Valid
	private ADNRegistroPlanFuturoRespaldoRequestDTO respaldo;
	
	@Valid
	private ADNRegistroPlanFuturoCapitalRequestDTO capitalProteger;

	public ADNRegistroPlanFuturoBaseRequestDTO getBase() {
		return base;
	}

	public void setBase(ADNRegistroPlanFuturoBaseRequestDTO base) {
		this.base = base;
	}

	public ADNRegistroPlanFuturoRespaldoRequestDTO getRespaldo() {
		return respaldo;
	}

	public void setRespaldo(ADNRegistroPlanFuturoRespaldoRequestDTO respaldo) {
		this.respaldo = respaldo;
	}

	public ADNRegistroPlanFuturoCapitalRequestDTO getCapitalProteger() {
		return capitalProteger;
	}

	public void setCapitalProteger(ADNRegistroPlanFuturoCapitalRequestDTO capitalProteger) {
		this.capitalProteger = capitalProteger;
	}
	
	
}

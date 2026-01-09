package pe.interseguro.siv.common.persistence.rest.crm.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidarCreacionAsignacionRequest implements Serializable {

	private ValidarCreacionAsignacionDetRequest param;

	public ValidarCreacionAsignacionDetRequest getParam() {
		return param;
	}

	public void setParam(ValidarCreacionAsignacionDetRequest param) {
		this.param = param;
	}

	
}

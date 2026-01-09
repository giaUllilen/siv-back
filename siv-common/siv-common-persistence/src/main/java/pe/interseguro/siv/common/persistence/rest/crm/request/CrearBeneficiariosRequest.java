package pe.interseguro.siv.common.persistence.rest.crm.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearBeneficiariosRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3896275148336103464L;

	private CrearBeneficiariosParamRequest param;
}

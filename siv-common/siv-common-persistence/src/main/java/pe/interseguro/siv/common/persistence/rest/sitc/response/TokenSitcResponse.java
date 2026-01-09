package pe.interseguro.siv.common.persistence.rest.sitc.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenSitcResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4908292479265638754L;

	private String registrarTokenResult;
}

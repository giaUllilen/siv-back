package pe.interseguro.siv.common.persistence.rest.vtigger.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearTokenResultResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1955081716553130614L;
	
	private String token;
	private Long serverTime;
	private Long expireTime;
}

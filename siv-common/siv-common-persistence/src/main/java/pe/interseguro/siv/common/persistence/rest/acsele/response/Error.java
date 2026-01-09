package pe.interseguro.siv.common.persistence.rest.acsele.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Error implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4389702757477997064L;
	private String errorMessage;
	private String errorCode;
}

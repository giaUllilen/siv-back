package pe.interseguro.siv.common.persistence.rest.vidafree.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenericoResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 225067552505691768L;

	private Integer status;
	private String code;
	private String title;
	private String message;
}

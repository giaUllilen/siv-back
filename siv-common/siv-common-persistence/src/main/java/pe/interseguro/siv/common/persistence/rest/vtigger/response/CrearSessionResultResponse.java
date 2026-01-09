package pe.interseguro.siv.common.persistence.rest.vtigger.response;

import java.io.Serializable;

import lombok.Data;

@Data
public class CrearSessionResultResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2183538237443789152L;

	private String sessionName;
	private String userId;
	private String version;
	private String vtigerVersion;
}

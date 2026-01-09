package pe.interseguro.siv.common.persistence.rest.vtigger.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsultaUsuarioRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8439205312521024666L;

	private String id;
}

package pe.interseguro.siv.common.exception;

import org.springframework.validation.Errors;

/**
 * Clase Exception para Base de Datos
 * 
 * @author ti-is
 */
public class SivDBException extends SivBaseException {

	private static final long serialVersionUID = -321198944673468713L;

	private Errors errors;

	
	public SivDBException(String message, Errors errors) {
		super(message);
		this.errors = errors;
	}

	public Errors getErrors() {
		return errors;
	}
	

}

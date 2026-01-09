package pe.interseguro.siv.common.exception;

import org.springframework.validation.Errors;

/**
 * Clase Exception para SOA (Rest, SOAP, JMS) 
 * 
 * @author ti-is
 */
public class SivSOAException extends SivBaseException {

	private static final long serialVersionUID = -4432439686788994771L;

	private Errors errors;

	
	public SivSOAException(String message, Errors errors) {
		super(message);
		this.errors = errors;
	}

	public Errors getErrors() {
		return errors;
	}
	
	
}

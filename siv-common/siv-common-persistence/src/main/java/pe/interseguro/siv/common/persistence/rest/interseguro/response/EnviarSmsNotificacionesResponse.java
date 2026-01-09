package pe.interseguro.siv.common.persistence.rest.interseguro.response;

import java.io.Serializable;


public class EnviarSmsNotificacionesResponse implements Serializable {
	private String status;
	private Object message;
	private String error;
	
	
	public Object getMessage() {
		return message;
	}
	public void setMessage(Object message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	
}

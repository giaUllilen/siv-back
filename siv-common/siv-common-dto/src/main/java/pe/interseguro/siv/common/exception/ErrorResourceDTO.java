package pe.interseguro.siv.common.exception;

import java.util.List;

/**
 * Clase DTO para capturas de errores
 * 
 * @author ti-is
 *
 */
public class ErrorResourceDTO {

	private String code;
	private String message;
	private String service;
	private String request;
	private List<FieldErrorResourceDTO> fieldErrors;

	public ErrorResourceDTO() {
	}

	public ErrorResourceDTO(String code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public ErrorResourceDTO(String code, String message, String service) {
		this.code = code;
		this.message = message;
		this.service = service;
	}
	
	public ErrorResourceDTO(String code, String message, String service, String request) {
		this.code = code;
		this.message = message;
		this.service = service;
		this.request = request;
	}

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<FieldErrorResourceDTO> getFieldErrors() {
		return fieldErrors;
	}
	public void setFieldErrors(List<FieldErrorResourceDTO> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}
	
}
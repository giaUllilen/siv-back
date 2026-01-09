package pe.interseguro.siv.admin.errorhandling;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter

public class ApiError {
	private String codigoRespuesta;
	private HttpStatus status;
	@JsonProperty("mensajeRespuesta")
	private String message;
	private List<String> errors;
	public ApiError() {
		super();
	}

	public ApiError(final HttpStatus status,final String codigoRespuesta, final String message, final List<String> errors) {
		super();
		this.status = status;
		this.message = message;
		this.errors = errors;
		this.codigoRespuesta=codigoRespuesta;
	}

	public ApiError(final HttpStatus status, final String codigoRespuesta, final String message, final String error) {
		super();
		this.status = status;
		this.message = message;
		this.codigoRespuesta=codigoRespuesta;
		errors = Arrays.asList(error);
	}
}

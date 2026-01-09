package pe.interseguro.siv.common.persistence.rest.interseguro.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EnviarSmsResponse implements Serializable {
	@JsonProperty("message")
	private String estado;
	@JsonProperty("code")
	private String codigo;
	@JsonProperty("status")
	private String status;
	@JsonProperty("error")
	private String error;
	
	
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
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

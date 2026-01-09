package pe.interseguro.siv.common.persistence.rest.crm.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import pe.interseguro.siv.common.persistence.rest.niubiz.response.BaseResponse;

@Getter
@Setter
public class ValidarCreacionAsignacionResponse extends BaseResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("IdOportunidad")
	private String idOportunidad;
	
	private String mensaje;

	@JsonProperty("mensaje_crm")
	private String mensajeCrm;
	
	private Boolean respuesta;
	public String getIdOportunidad() {
		return idOportunidad;
	}
	public void setIdOportunidad(String idOportunidad) {
		this.idOportunidad = idOportunidad;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public Boolean getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(Boolean respuesta) {
		this.respuesta = respuesta;
	}
	
}

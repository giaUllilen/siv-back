/**
 * 
 */
package pe.interseguro.siv.common.persistence.rest.interseguro.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author digital-is
 *
 */
public class EnviarSmsRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private String from;
	private String messageType;
	@JsonProperty("to")
	private String celular;	
	@JsonProperty("text")
	private String mensaje;
	@JsonProperty("nroDocumento")
	private String nroDocumento;
	@JsonProperty("motivoNotificacion")
	private String motivoNotificacion;
	private String usuario;
	
	
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getCelular() {
		return celular;
	}
	public void setCelular(String celular) {
		this.celular = celular;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public String getNroDocumento() {
		return nroDocumento;
	}
	public void setNroDocumento(String nroDocumento) {
		this.nroDocumento = nroDocumento;
	}
	public String getMotivoNotificacion() {
		return motivoNotificacion;
	}
	public void setMotivoNotificacion(String motivoNotificacion) {
		this.motivoNotificacion = motivoNotificacion;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
}

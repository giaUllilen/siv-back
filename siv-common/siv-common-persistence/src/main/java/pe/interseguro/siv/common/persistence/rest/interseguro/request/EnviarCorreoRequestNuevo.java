package pe.interseguro.siv.common.persistence.rest.interseguro.request;

import java.io.Serializable;
import java.util.List;

import pe.interseguro.siv.common.persistence.rest.interseguro.dto.Adjuntos;
import pe.interseguro.siv.common.persistence.rest.interseguro.dto.Copia;
import pe.interseguro.siv.common.persistence.rest.interseguro.dto.CopiaOculta;
import pe.interseguro.siv.common.persistence.rest.interseguro.dto.Remitente;

public class EnviarCorreoRequestNuevo implements Serializable {
	
	private static final long serialVersionUID = -5321537298978188130L;

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getCotizacion() {
		return cotizacion;
	}

	public void setCotizacion(String cotizacion) {
		this.cotizacion = cotizacion;
	}

	public String getNro_documento() {
		return nro_documento;
	}

	public void setNro_documento(String nro_documento) {
		this.nro_documento = nro_documento;
	}

	public String getMotivo_notificacion() {
		return motivo_notificacion;
	}

	public void setMotivo_notificacion(String motivo_notificacion) {
		this.motivo_notificacion = motivo_notificacion;
	}

	private String cotizacion;
	private String nro_documento;
	private String motivo_notificacion;
	private String priority;
	private String title;
	private String subject;
	private String htmlContent;
	private List<Remitente> to;
	private List<Copia> cc;
	private List<CopiaOculta> bcc;
	private List<Adjuntos> attachments;
	private String usuario;

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getTitle() {
		return title;
	}

	public void setTo(List<Remitente> to) {
		this.to = to;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getHtmlContent() {
		return htmlContent;
	}

	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}

	public List<Remitente> getTo() {
		return to;
	}

	public List<Copia> getCc() {
		return cc;
	}

	public void setCc(List<Copia> cc) {
		this.cc = cc;
	}

	public List<CopiaOculta> getBcc() {
		return bcc;
	}

	public void setBcc(List<CopiaOculta> bcc) {
		this.bcc = bcc;
	}

	public List<Adjuntos> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Adjuntos> attachments) {
		this.attachments = attachments;
	}

}

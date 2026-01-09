/**
 * 
 */
package pe.interseguro.siv.common.persistence.rest.interseguro.request;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author digital-is
 *
 */
public class EnviarCorreoRequest implements Serializable {

	@JsonProperty("p_remitente")
	private String pRemitente;
	
	@JsonProperty("p_displayName")
	private String pDisplayName;
	
	@JsonProperty("p_destinatario")
	private String pDestinatario;

	@JsonProperty("p_asunto")
	private String pAsunto;

	@JsonProperty("p_mensaje")
	private String pMensaje;
	
	@JsonProperty("p_ruta_archivo_adjunto")
	private List<String> pRutaArchivoAdjunto;

	
	public String getpRemitente() {
		return pRemitente;
	}
	public void setpRemitente(String pRemitente) {
		this.pRemitente = pRemitente;
	}
	public String getpDisplayName() {
		return pDisplayName;
	}
	public void setpDisplayName(String pDisplayName) {
		this.pDisplayName = pDisplayName;
	}
	public String getpDestinatario() {
		return pDestinatario;
	}
	public void setpDestinatario(String pDestinatario) {
		this.pDestinatario = pDestinatario;
	}
	public String getpAsunto() {
		return pAsunto;
	}
	public void setpAsunto(String pAsunto) {
		this.pAsunto = pAsunto;
	}
	public String getpMensaje() {
		return pMensaje;
	}
	public void setpMensaje(String pMensaje) {
		this.pMensaje = pMensaje;
	}
	public List<String> getpRutaArchivoAdjunto() {
		return pRutaArchivoAdjunto;
	}
	public void setpRutaArchivoAdjunto(List<String> pRutaArchivoAdjunto) {
		this.pRutaArchivoAdjunto = pRutaArchivoAdjunto;
	}
	
	
}

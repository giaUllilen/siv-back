package pe.interseguro.siv.common.persistence.rest.interseguro.request;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EnviarCorreoSMERequest implements Serializable {

	@JsonProperty("De")
	private String De;
	
	@JsonProperty("DeNombre")
	private String DeNombre;
	
	@JsonProperty("Para")
	private String Para;
	
	@JsonProperty("ResponderA")
	private String ResponderA;
	
	@JsonProperty("ResponderANombre")
	private String ResponderANombre;
	
	@JsonProperty("Asunto")
	private String Asunto;
	
	@JsonProperty("Cuerpo")
	private String Cuerpo;
	
	@JsonProperty("RutasArchivosAdjuntos")
	private List<String> RutasArchivosAdjuntos;
	
	public String getDe() {
		return De;
	}
	public void setDe(String de) {
		De = de;
	}
	public String getDeNombre() {
		return DeNombre;
	}
	public void setDeNombre(String deNombre) {
		DeNombre = deNombre;
	}
	public String getPara() {
		return Para;
	}
	public void setPara(String para) {
		Para = para;
	}
	public String getResponderA() {
		return ResponderA;
	}
	public void setResponderA(String responderA) {
		ResponderA = responderA;
	}
	public String getResponderANombre() {
		return ResponderANombre;
	}
	public void setResponderANombre(String responderANombre) {
		ResponderANombre = responderANombre;
	}
	public String getAsunto() {
		return Asunto;
	}
	public void setAsunto(String asunto) {
		Asunto = asunto;
	}
	public String getCuerpo() {
		return Cuerpo;
	}
	public void setCuerpo(String cuerpo) {
		Cuerpo = cuerpo;
	}
	public List<String> getRutasArchivosAdjuntos() {
		return RutasArchivosAdjuntos;
	}
	public void setRutasArchivosAdjuntos(List<String> rutasArchivosAdjuntos) {
		RutasArchivosAdjuntos = rutasArchivosAdjuntos;
	}
	
	
	
}

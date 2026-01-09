package pe.interseguro.siv.common.bean;

import java.io.Serializable;

public class DocumentoADNReferido implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6182527046652403051L;

	private String nombres;
	private String telefono;
	
	public DocumentoADNReferido() {}
	
	public DocumentoADNReferido(String nombres, String telefono) {
		super();
		this.nombres = nombres;
		this.telefono = telefono;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
}

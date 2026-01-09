package pe.interseguro.siv.common.bean;

import java.io.Serializable;

public class DocumentoADNFamiliar implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4224053674067771836L;
	
	private String nombres;
	private String relacion;
	private String edad;
	
	public DocumentoADNFamiliar(String nombres, String relacion, String edad) {
		super();
		this.nombres = nombres;
		this.relacion = relacion;
		this.edad = edad;
	}
	
	public String getNombres() {
		return nombres;
	}
	public void setNombres(String nombres) {
		this.nombres = nombres;
	}
	public String getRelacion() {
		return relacion;
	}
	public void setRelacion(String relacion) {
		this.relacion = relacion;
	}
	public String getEdad() {
		return edad;
	}
	public void setEdad(String edad) {
		this.edad = edad;
	}
	
}

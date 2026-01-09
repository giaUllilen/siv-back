package pe.interseguro.siv.common.bean;

import java.io.Serializable;

/**
 * Clase Pojo generica para utilizarse como key / value
 * 
 * @author ti-is
 */
public class GenericoComboBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7257459795191391388L;
	
	private String codigo;
	private String codigoAuxiliar;
	private String descripcion;
	
	
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getCodigoAuxiliar() {
		return codigoAuxiliar;
	}
	public void setCodigoAuxiliar(String codigoAuxiliar) {
		this.codigoAuxiliar = codigoAuxiliar;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	

}

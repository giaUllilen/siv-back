package pe.interseguro.siv.common.dto.response;

import java.io.Serializable;

public class CulqiBaseDetalleResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7822054696798194365L;

	private String fechaProceso;
	private String monto;
	private String moneda;
	private String estadoCodigo;
	private String estadoDescripcion;
	private String mensajeComercial;
	private String mensajeUsuario;
	private String comision;
	
	public String getFechaProceso() {
		return fechaProceso;
	}
	public void setFechaProceso(String fechaProceso) {
		this.fechaProceso = fechaProceso;
	}
	public String getMonto() {
		return monto;
	}
	public void setMonto(String monto) {
		this.monto = monto;
	}
	public String getMoneda() {
		return moneda;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	public String getEstadoCodigo() {
		return estadoCodigo;
	}
	public void setEstadoCodigo(String estadoCodigo) {
		this.estadoCodigo = estadoCodigo;
	}
	public String getEstadoDescripcion() {
		return estadoDescripcion;
	}
	public void setEstadoDescripcion(String estadoDescripcion) {
		this.estadoDescripcion = estadoDescripcion;
	}
	public String getMensajeComercial() {
		return mensajeComercial;
	}
	public void setMensajeComercial(String mensajeComercial) {
		this.mensajeComercial = mensajeComercial;
	}
	public String getMensajeUsuario() {
		return mensajeUsuario;
	}
	public void setMensajeUsuario(String mensajeUsuario) {
		this.mensajeUsuario = mensajeUsuario;
	}
	public String getComision() {
		return comision;
	}
	public void setComision(String comision) {
		this.comision = comision;
	}
}

package pe.interseguro.siv.common.dto.response;

import java.io.Serializable;

public class AcpFormularioCuentaResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8498171408295896973L;

	private String entidadBancaria;
	private String tipoCuenta;
	private String moneda;
	private String numeroCuenta;
	
	public String getEntidadBancaria() {
		return entidadBancaria;
	}
	public void setEntidadBancaria(String entidadBancaria) {
		this.entidadBancaria = entidadBancaria;
	}
	public String getTipoCuenta() {
		return tipoCuenta;
	}
	public void setTipoCuenta(String tipoCuenta) {
		this.tipoCuenta = tipoCuenta;
	}
	public String getMoneda() {
		return moneda;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	public String getNumeroCuenta() {
		return numeroCuenta;
	}
	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}
}

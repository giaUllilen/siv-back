package pe.interseguro.siv.common.dto.response;

import java.io.Serializable;

public class AcpFormularioTarjetaResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6593596835779396958L;
	
	private String entidadFinanciera;
	private String numeroTarjeta;
	private String fechaVencimiento;
	private String ccv;
	
	public String getNumeroTarjeta() {
		return numeroTarjeta;
	}
	public void setNumeroTarjeta(String numeroTarjeta) {
		this.numeroTarjeta = numeroTarjeta;
	}
	public String getFechaVencimiento() {
		return fechaVencimiento;
	}
	public void setFechaVencimiento(String fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}
	public String getCcv() {
		return ccv;
	}
	public void setCcv(String ccv) {
		this.ccv = ccv;
	}
	public String getEntidadFinanciera() {
		return entidadFinanciera;
	}
	public void setEntidadFinanciera(String entidadFinanciera) {
		this.entidadFinanciera = entidadFinanciera;
	}

}

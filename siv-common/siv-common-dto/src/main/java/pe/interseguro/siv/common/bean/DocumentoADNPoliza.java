package pe.interseguro.siv.common.bean;

import java.io.Serializable;

public class DocumentoADNPoliza implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7091004514940389356L;
	
	private String numeroPoliza;
	private String fechaEmision;
	private String nombreProducto;
	private String estadoPoliza;
	private String pagadoHasta;
	private String moneda;
	private String montoCoberturaPrincipal;
	private String coberturaVidaAdicional;
	
	public DocumentoADNPoliza() {}

	public DocumentoADNPoliza(String numeroPoliza, String fechaEmision, String nombreProducto, String estadoPoliza,
			String pagadoHasta, String moneda, String montoCoberturaPrincipal, String coberturaVidaAdicional) {
		super();
		this.numeroPoliza = numeroPoliza;
		this.fechaEmision = fechaEmision;
		this.nombreProducto = nombreProducto;
		this.estadoPoliza = estadoPoliza;
		this.pagadoHasta = pagadoHasta;
		this.moneda = moneda;
		this.montoCoberturaPrincipal = montoCoberturaPrincipal;
		this.coberturaVidaAdicional = coberturaVidaAdicional;
	}

	public String getNumeroPoliza() {
		return numeroPoliza;
	}

	public void setNumeroPoliza(String numeroPoliza) {
		this.numeroPoliza = numeroPoliza;
	}

	public String getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(String fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public String getNombreProducto() {
		return nombreProducto;
	}

	public void setNombreProducto(String nombreProducto) {
		this.nombreProducto = nombreProducto;
	}

	public String getEstadoPoliza() {
		return estadoPoliza;
	}

	public void setEstadoPoliza(String estadoPoliza) {
		this.estadoPoliza = estadoPoliza;
	}

	public String getPagadoHasta() {
		return pagadoHasta;
	}

	public void setPagadoHasta(String pagadoHasta) {
		this.pagadoHasta = pagadoHasta;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public String getMontoCoberturaPrincipal() {
		return montoCoberturaPrincipal;
	}

	public void setMontoCoberturaPrincipal(String montoCoberturaPrincipal) {
		this.montoCoberturaPrincipal = montoCoberturaPrincipal;
	}

	public String getCoberturaVidaAdicional() {
		return coberturaVidaAdicional;
	}

	public void setCoberturaVidaAdicional(String coberturaVidaAdicional) {
		this.coberturaVidaAdicional = coberturaVidaAdicional;
	}

}

package pe.interseguro.siv.common.persistence.db.sitc.bean;

public class PolizaCobro {
	private String numeroPoliza;
	private String cliente;
	private String emailEnvioPoliza;
	private String frecuenciaPago;
	private String estadoAfiliacion;
	private String monedaPago;
	private String montoPago;
	private String viaCobro;
	private String numeroTarjeta;
	private String fechaPago;
	private String producto;
	
	public String getNumeroPoliza() {
		return numeroPoliza;
	}
	public void setNumeroPoliza(String numeroPoliza) {
		this.numeroPoliza = numeroPoliza;
	}
	public String getCliente() {
		return cliente;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	public String getEmailEnvioPoliza() {
		return emailEnvioPoliza;
	}
	public void setEmailEnvioPoliza(String emailEnvioPoliza) {
		this.emailEnvioPoliza = emailEnvioPoliza;
	}
	public String getFrecuenciaPago() {
		return frecuenciaPago;
	}
	public void setFrecuenciaPago(String frecuenciaPago) {
		this.frecuenciaPago = frecuenciaPago;
	}
	public String getEstadoAfiliacion() {
		return estadoAfiliacion;
	}
	public void setEstadoAfiliacion(String estadoAfiliacion) {
		this.estadoAfiliacion = estadoAfiliacion;
	}
	public String getMonedaPago() {
		return monedaPago;
	}
	public void setMonedaPago(String monedaPago) {
		this.monedaPago = monedaPago;
	}
	public String getMontoPago() {
		return montoPago;
	}
	public void setMontoPago(String montoPago) {
		this.montoPago = montoPago;
	}
	public String getViaCobro() {
		return viaCobro;
	}
	public void setViaCobro(String viaCobro) {
		this.viaCobro = viaCobro;
	}
	public String getNumeroTarjeta() {
		return numeroTarjeta;
	}
	public void setNumeroTarjeta(String numeroTarjeta) {
		this.numeroTarjeta = numeroTarjeta;
	}
	public String getFechaPago() {
		return fechaPago;
	}
	public void setFechaPago(String fechaPago) {
		this.fechaPago = fechaPago;
	}
	public String getProducto() {
		return producto;
	}
	public void setProducto(String producto) {
		this.producto = producto;
	}
}

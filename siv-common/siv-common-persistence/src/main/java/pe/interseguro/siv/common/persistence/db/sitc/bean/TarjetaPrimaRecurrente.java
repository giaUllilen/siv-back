package pe.interseguro.siv.common.persistence.db.sitc.bean;

public class TarjetaPrimaRecurrente {
	private String tarjeta;
	private String tarjetaVencimiento;
	private String viaCobro;
	private String medioPago;
	private String moneda;
	private String tipoCuenta;
	
	public String getTarjeta() {
		return tarjeta;
	}
	public void setTarjeta(String tarjeta) {
		this.tarjeta = tarjeta;
	}
	public String getTarjetaVencimiento() {
		return tarjetaVencimiento;
	}
	public void setTarjetaVencimiento(String tarjetaVencimiento) {
		this.tarjetaVencimiento = tarjetaVencimiento;
	}
	public String getViaCobro() {
		return viaCobro;
	}
	public void setViaCobro(String viaCobro) {
		this.viaCobro = viaCobro;
	}
	public String getMedioPago() {
		return medioPago;
	}
	public void setMedioPago(String medioPago) {
		this.medioPago = medioPago;
	}
	public String getMoneda() {
		return moneda;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	public String getTipoCuenta() {
		return tipoCuenta;
	}
	public void setTipoCuenta(String tipoCuenta) {
		this.tipoCuenta = tipoCuenta;
	}
}

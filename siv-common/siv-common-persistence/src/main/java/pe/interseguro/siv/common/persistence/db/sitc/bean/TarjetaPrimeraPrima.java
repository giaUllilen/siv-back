package pe.interseguro.siv.common.persistence.db.sitc.bean;

public class TarjetaPrimeraPrima {
	private String idAfiliacionPropuesta;
	private String idTarjetaRecurrenteSitc;
	private String tipoViaCobro;
	private String viaCobro;
	private String moneda;
	private String tarjeta;
	private String tarjetaVencimiento;
	
	public String getIdAfiliacionPropuesta() {
		return idAfiliacionPropuesta;
	}
	public void setIdAfiliacionPropuesta(String idAfiliacionPropuesta) {
		this.idAfiliacionPropuesta = idAfiliacionPropuesta;
	}
	public String getTipoViaCobro() {
		return tipoViaCobro;
	}
	public void setTipoViaCobro(String tipoViaCobro) {
		this.tipoViaCobro = tipoViaCobro;
	}
	public String getViaCobro() {
		return viaCobro;
	}
	public void setViaCobro(String viaCobro) {
		this.viaCobro = viaCobro;
	}
	public String getMoneda() {
		return moneda;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
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
	public String getIdTarjetaRecurrenteSitc() {
		return idTarjetaRecurrenteSitc;
	}
	public void setIdTarjetaRecurrenteSitc(String idTarjetaRecurrenteSitc) {
		this.idTarjetaRecurrenteSitc = idTarjetaRecurrenteSitc;
	}
}

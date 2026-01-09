package pe.interseguro.siv.common.persistence.db.acsele.bean;

public class TipoCambio {
	private String monedaOrigen;
	private String monedaDestino;
	private String fecha;
	private Double conversion;
	
	public String getMonedaOrigen() {
		return monedaOrigen;
	}
	public void setMonedaOrigen(String monedaOrigen) {
		this.monedaOrigen = monedaOrigen;
	}
	public String getMonedaDestino() {
		return monedaDestino;
	}
	public void setMonedaDestino(String monedaDestino) {
		this.monedaDestino = monedaDestino;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public Double getConversion() {
		return conversion;
	}
	public void setConversion(Double conversion) {
		this.conversion = conversion;
	}
}

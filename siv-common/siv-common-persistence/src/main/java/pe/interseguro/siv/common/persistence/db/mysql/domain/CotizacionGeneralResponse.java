package pe.interseguro.siv.common.persistence.db.mysql.domain;

import java.util.Date;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class CotizacionGeneralResponse {
	
	private String nroCotizacion;
	private String fechaCotizacion;
	private String asegurado;
	private String tipoProducto;
	private String planId;
	private String planNombre;
	private String subplanId;
	private String periodoPago;
	private String monedaValor;
    private String monedaSimbolo;
    private String numeroDocumento;
    private String subplan;
    private String primaPagoTotal;
	private String primaAnual;
	private String moneda;
	private String nroCotizacionOriginal;
	private String estado;
	private String estadoGeneral;
    private Date fechaRegistroSamp;
    private String crmOportunidadId;
    private String crmCotizadorId;
    
	@Override
	public String toString() {
		return "CotizacionGeneralResponse [nroCotizacion=" + nroCotizacion + ", fechaCotizacion=" + fechaCotizacion
				+ ", asegurado=" + asegurado + ", tipoProducto=" + tipoProducto + ", planId=" + planId + ", planNombre="
				+ planNombre + ", subplanId=" + subplanId + ", periodoPago=" + periodoPago + ", monedaValor="
				+ monedaValor + ", monedaSimbolo=" + monedaSimbolo + ", numeroDocumento=" + numeroDocumento
				+ ", subplan=" + subplan + ", primaPagoTotal=" + primaPagoTotal + ", primaAnual=" + primaAnual
				+ ", moneda=" + moneda + ", nroCotizacionOriginal=" + nroCotizacionOriginal + ", estado=" + estado
				+ ", estadoGeneral=" + estadoGeneral + ", fechaRegistroSamp=" + fechaRegistroSamp
				+ ", crmOportunidadId=" + crmOportunidadId + ", crmCotizadorId=" + crmCotizadorId + "]";
	}
    
    

}

package pe.interseguro.siv.common.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class CotizaListaItemResponseDTO {
	
	private String nroCotizacion;
	private String nroCotizacionOriginal;
	private String fechaCotizacion;
	private String productoId;
	private String asegurado;
	private String productoNombre;
	private String planId;
	private String planNombre;
	private String periodoPago;
	private String monedaSimbolo;
	private String primaBruta;
	private String primaPagoTotal;
	private String numeroDocumento;
	private String grupoFamiliar;
	private String subplanId;
	private String subplan;
	private String oportunidadId;
	private String crmCotizadorId;
	private String urlReporte;
	private String urlReporteADN;
	private String estadoSolicitud;
}
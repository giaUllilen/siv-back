package pe.interseguro.siv.common.bean;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class DocumentoCotizacion {
	private String numeroCotizacion;
	private String asegurable;
	private String fechaNacimiento;
	private String sexo;
	private String documentoIdentidad;
	private String producto;
	private String plan;
	private String subplan;
	private String periodoCobertura;
	private String nombreperiodoCobertura;
	private String periodoPagoPrima;
	private String periodoPagoBeneficio;
	private String frecuenciaPago;
	private String moneda;
	private String monedaSimbolo;
	private String devolucion;
	private String montoDevolucion;
	private String primaAnual;
	private String factorPago;
	private String primaComercial;
	private String primaComercialTotal;
	private String igv;
	private String agente;
	private String fechaCotizacion;
	private String fechaValidacionCotizacion;
	private String tcea;
	private String edadActuarial;
	private String capitalAsegurado;
	private String fechaTerminoTabla;
	private String coberturaPrincipalNombre;
	private String coberturaPrincipalMonto;
	private String coberturaPrincipalTasa;
	private String coberturaPrincipalEdadMinima;
	private String coberturaPrincipalEdadMaxima;
	private String coberturaPrincipalEdadPerm;
	private String coberturaPrincipalVigencia;
	private String coberturaPrincipalPrimaAnual;
	private String tir;
	private String fondoGarantizado;
}

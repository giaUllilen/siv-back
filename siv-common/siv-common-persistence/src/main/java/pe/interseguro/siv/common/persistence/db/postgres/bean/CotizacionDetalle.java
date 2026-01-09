package pe.interseguro.siv.common.persistence.db.postgres.bean;

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
public class CotizacionDetalle {

	private String nroCotizacion;
	private String cumulo;
	private String edad;
	private Date fechaCotizacion;
	private String moneda;
	private String monedaId;
	private Float montoTotalFondoGarantizado;
	private String frecuenciaPagoPrima;
	private Integer periodoCobertura;
	private Integer nroAnualidadesPagoBeneficio;
	private String planId;
	private String plan;
	private String planCuotaComodin;
	private String planCuotaDoble;
	private String fumador;
	private Float primaComercialAnual;
	private Float factorPago;
	private Float primaComercial;
	private String subplanId;
	private String subplan;
	private String tirGarantizada;
	private String coberturaId;
	private String coberturaNombre;
	private String coberturaTipo;
	private Float coberturaCapital;
	private Float coberturaPrima;
	private String crmOportunidadId;
	private String crmCotizadorId;
	private String per_cob;
	private String nomPerCob;
	private String sex;
	private String feNac;
	private String numDoc;
	private String tcea;
	private String igv;
	private String factor;
	private String primaComTotal;
}
package pe.interseguro.siv.common.bean;

import java.io.Serializable;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class SolicitudPDFProductoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1154053080908540837L;
	
	private boolean checkMonedaFondoSoles;
	private boolean checkMonedaFondoDolares;
	private String montoFondoGarantizado;
	private String periodoCoberturaAnual;
	private String anualidadPago;
	private boolean checkFrecuenciaMensual;
	private boolean checkFrecuenciaTrimestral;
	private boolean checkFrecuenciaSemestral;
	private boolean checkFrecuenciaAnual;
	private boolean checkFrecuenciaUnica;
	private boolean checkCuotaComodin;
	private boolean checkCuotaDoble;
	private boolean checkTipoRiesgoFumadorSi;
	private boolean checkTipoRiesgoFumadorNo;
	private boolean checkPrimaMonedaSoles;
	private boolean checkPrimaMonedaDolares;
	private boolean checkDevolucionPrima0;
	private boolean checkDevolucionPrima50;
	private boolean checkDevolucionPrima75;
	private boolean checkDevolucionPrima100;
	private boolean checkDevolucionPrima125;
	private boolean checkDevolucionPrima130;
	private boolean checkDevolucionPrima135;
	private boolean checkDevolucionPrima140;
	private boolean checkDevolucionPrima145;
	private boolean checkDevolucionPrima150;
	private boolean checkTemporal;
	private boolean checkVitalicio;
	private List<SolicitudPDFProductoDetalleBean> detallePrimas;
	private String primaComercialAnual;
	private String factorPago;
	private String primaComercial;
	
	private String primaVidaFreePrimaComercialAnual;
	private String primaVidaFreeFactorPago;
	private String primaVidaFreePrimaComercial;
	private String primaVidaFreePrimaComercialIgv;
	private String primaVidaFreePrimaComercialTotal;
	
}
 

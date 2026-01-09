package pe.interseguro.siv.common.dto.response;

import java.util.Date;
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
public class CotizaDetalleResponseDTO extends BaseResponseDTO {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -6280049522415374302L;
	
	private String nroCotizacion;
	private Date fechaCotizacion;
	private String moneda;
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
	private Float primaIgv;
	private Integer primaDevolucion;
	private String subplanId;
	private String subplan;
	private String tirGarantizada;
	private String crmOportunidadId;
	private String crmCotizadorId;
	
	private List<CotizaDetalleCoberturaResponseDTO> cobertura;

}
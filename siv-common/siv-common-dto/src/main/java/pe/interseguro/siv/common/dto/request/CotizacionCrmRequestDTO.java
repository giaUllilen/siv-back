package pe.interseguro.siv.common.dto.request;

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
public class CotizacionCrmRequestDTO implements Serializable  {/**
	 * 
	 */
	private static final long serialVersionUID = -499490762050956668L;
	
	private String idCRM;
    private String idOportunidadCRM;
    private CotizadorCrmContactoDTO beContacto;
    private String idProducto;
    private String nombreProducto;
    private String idPlan;
    private String nombrePlan;
    private String vigencia;
    private String fechaInicial;
    private String fechaFinal;
    private String tipoDocumento;
    private String numeroDocumento;
    private String montoPrima;
    private String primaFormaPago;
    private String numeroCotizacion;
    private String planVida;
    private String opcionVida;
    private String tipoVigencia;
    private String fechaMovimiento;
    private String tipoMoneda;
    private String moneda;
    private String monedaPrima;
    private String frecuenciaPago;
    private String aplicaDescuento;
    private String tipoDescuento;
    private String profesionActividad;
    private String grupoFamiliar;
    private String claseAccPers;
    private String opcionTD;
    private String nivelAgrupReaseg;
    private CotizadorCrmPlanFinanciamientoDTO planFinanciamiento;
    private List<CotizadorCrmUnidadRiesgo> unidadesDeRiesgo;
    
    private String usuarioLogin;
    private String agenteNombres;
    private String agenteCorreo;
    private String agenteNumVendedor;
    private String agenteIdCRM;
}

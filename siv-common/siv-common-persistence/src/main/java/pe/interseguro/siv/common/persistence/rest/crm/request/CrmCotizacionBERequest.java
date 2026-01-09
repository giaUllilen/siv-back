package pe.interseguro.siv.common.persistence.rest.crm.request;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrmCotizacionBERequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3755648173042701924L;

	private String idCRM;
    private String idOportunidadCRM;
    private CrmBEContacto beContacto;
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
    private CrmPlanFinanciamiento planFinanciamiento;
    private List<CrmUnidadDeRiesgo> unidadesDeRiesgo;
}

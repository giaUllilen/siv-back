package pe.interseguro.siv.common.persistence.rest.vidafree.response;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import pe.interseguro.siv.common.bean.DocumentoCotizacionDevolucion;

@Getter
@Setter
public class CotizacionDetalleResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5159928087509481579L;

	private String idCotizacion;
    private String codProducto;
    private String codProductoAcsele;
    private String nombreProducto;
    private String codPlan;
    private String nombrePlan;
    private String codPlanAcsele;
    private String numeroCotizacion;
    private String fechaCotizacion;
    private String fechaVigencia;
    private String codMoneda;
    private String codMonedaAcsele;
    private String codMonedaSimbolo;
    private String monedaDescripcion;
    private String cumuloMoneda;
    private String cumuloMonto;
    private String factorPago;
    private Double igv;
    private Double tcea;
    private Integer porcentajeDevolucion;
    private Integer periodoVigencia;
    private Integer periodoPago;
    private String frecuenciaPago;
    private String frecuenciaPagoDescripcion;
    private Double primaRecurrenteTotal;
    private Double primaClienteAnualTotal;
    private Double montoDevolucionTotal;
    private List<CoberturaResponse> coberturas;
    private List<DocumentoCotizacionDevolucion> tablaDevolucion;
}

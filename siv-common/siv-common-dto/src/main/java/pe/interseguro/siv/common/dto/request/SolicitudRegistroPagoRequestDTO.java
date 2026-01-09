package pe.interseguro.siv.common.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class SolicitudRegistroPagoRequestDTO extends BaseRequestDTO {/**
	 * 
	 */
	private static final long serialVersionUID = 1458344789883254805L;

	private String documentoIdentidad;
	private String tipoDocumento;
	private String apePaterno;
	private String apeMaterno;
	private String nomPersona;
	private Long numeroPropuesta;
	private Long numeroPropuestaNew; //propuesta de historico
	private String moneda;
	private String numeroTarjeta;
	private String fechaVencimiento;
	private Integer tipoViaCobro;
    private String viaCobro;
    private Integer tipoCuenta;
    private Integer estado;
    private String numTelefono;
    private String codPeriodoPago;
    private String codRamo;
    private String codSubRamo; /*Debe venir una cadena concatenada el ramo y subramo*/
    private String fecIngresoMandato;
    private Integer numDiaCobro;
    private Float valPrimaFormaPago;
    private String usuario;
    private String numeroTarjetaPp;
    private String fechaVencimientoPp;
    private Integer tipoViaCobroPp;
    private Integer viaCobroPp;
    private String monedaPp;
    private Integer tipoCuentaPp;
    private String idAfiliacionPro;
    private String ip;
    private String idCulqi;
    private String pasarelaPp;
    private String pasarela;
    private String subscriptionId;
    private String tarjetaCobroRecurrente;
    private String tokenAfiliacion;
    private String tarjetaCcv;
}

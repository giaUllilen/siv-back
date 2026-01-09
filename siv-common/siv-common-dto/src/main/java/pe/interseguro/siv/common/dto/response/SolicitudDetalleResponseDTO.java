package pe.interseguro.siv.common.dto.response;

import java.io.Serializable;
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
public class SolicitudDetalleResponseDTO extends BaseResponseDTO {/**
	 * 
	 */
	private static final long serialVersionUID = 8208177355211315621L;

	private String numeroPropuesta;
	private String producto;
	private String plan;
	private String fechaCotizacion;
	private String frecuencia;
	private String primaComercial;
	private String primaIgv;
	private String aseguradoIgualContratante;
	private String jsonContratante;
	private String estadoSolicitud;
	private String fechaFirmaAsegurado;
	private String fechaFirmaContratante;
	private String fechaSolicitud;
	private Boolean linkEnviado;
	private Boolean linkUsado;
	private Boolean tienePagoRealizado;
	private String fechaPago;
	private String idAfiliacion;
	private String describePagoPrimero;
	private String describePagoRecurrente;
	private Boolean tieneRegistroSitc;
	private String fechaSitc;
	private Boolean tieneRegistroSamp;
	private String fechaSamp;
	private String codigoSms;
	private String codigoSmsContratante;
	private String pasarela;
	private String codigoTransaccion;
	private String moneda;
	private String estadoGeneral;
	private String estadoRecargo;
	private String estadoPropuesta;
	private String tipoEmision;
	private String flgProcesoEmision;
}

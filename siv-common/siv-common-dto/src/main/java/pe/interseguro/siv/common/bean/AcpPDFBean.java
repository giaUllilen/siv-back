package pe.interseguro.siv.common.bean;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@EqualsAndHashCode
public class AcpPDFBean implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = -3462326650631223170L;
	@JsonProperty("numero_propuesta")
	private String numeroPropuesta;
	@JsonProperty("producto")
	private String producto;
	@JsonProperty("tipo_contratante")
	private String tipoContratante;
	@JsonProperty("monto_primera_prima")
	private String montoPrimeraPrima;
	@JsonProperty("frecuencia_pago")
	private String frecuenciaPago;
	@JsonProperty("contratante")
	private AcpPDFPersonaBean contratante;
	@JsonProperty("tarjeta")
	private AcpPDFTarjetaBean tarjeta;
	@JsonProperty("cuenta")
	private AcpPDFCuentaBean cuenta;
	@JsonProperty("fecha_firma_dia")
	private String fechaFirmaDia;
	@JsonProperty("fecha_firma_mes")
	private String fechaFirmaMes;
	@JsonProperty("fecha_firma_anio")
	private String fechaFirmaAnio;
	@JsonProperty("agente_correo")
	private String agenteCorreo;
}

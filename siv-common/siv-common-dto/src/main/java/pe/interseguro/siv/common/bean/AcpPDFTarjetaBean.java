package pe.interseguro.siv.common.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class AcpPDFTarjetaBean implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 2473952284935177429L;
	
	private boolean checkVisa;
	private boolean checkMastercard;
	private boolean checkAmex;
	private boolean checkDiners;
	@JsonProperty("numero_tarjeta")
	private String numeroTarjeta;
	@JsonProperty("vencimiento_mes_1")
	private String vencimientoMes1;
	@JsonProperty("vencimiento_mes_2")
	private String vencimientoMes2;
	@JsonProperty("vencimiento_anio_1")
	private String vencimientoAnio1;
	@JsonProperty("vencimiento_anio_2")
	private String vencimientoAnio2;
	@JsonProperty("vencimiento_anio_3")
	private String vencimientoAnio3;
	@JsonProperty("vencimiento_anio_4")
	private String vencimientoAnio4;
}

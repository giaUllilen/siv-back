package pe.interseguro.siv.common.persistence.rest.global.response;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PolizaResponse implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8143774760668500446L;
	
	@JsonProperty("NUMERO_POLIZA")
	private String numero_poliza;
	
	@JsonProperty("FECHA_EMISION")
	private String fecha_emision;
	
	@JsonProperty("NOMBRE_PRODUCTO")
	private String nombreProducto;
	
	@JsonProperty("ESTADO")
	private String estado;
	
	@JsonProperty("MONEDA")
	private String moneda;
	
	@JsonProperty("MONTO_COBERTURA_PRINCIPAL")
	private String monto_cobertura_principal;
}

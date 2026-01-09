package pe.interseguro.siv.common.persistence.rest.global.response;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PolizaDetalleResponse implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8143774760668500446L;
	
	@JsonProperty("FECHA_PAGADO_HASTA")
	private String fecha_pagado_hasta;
	
	@JsonProperty("COBERTURA_VIDA_ADICIONAL")
	private String cobertura_vida_adicional;
	
}

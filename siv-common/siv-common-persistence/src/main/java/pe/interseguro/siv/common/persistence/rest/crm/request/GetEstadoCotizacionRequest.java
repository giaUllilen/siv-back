package pe.interseguro.siv.common.persistence.rest.crm.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetEstadoCotizacionRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8500492016949744671L;

	@JsonProperty("CotizacionId")
	private String cotizacionId;
}

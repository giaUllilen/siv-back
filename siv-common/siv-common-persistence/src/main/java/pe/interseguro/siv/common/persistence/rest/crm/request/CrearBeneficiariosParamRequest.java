package pe.interseguro.siv.common.persistence.rest.crm.request;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearBeneficiariosParamRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2392464204486060381L;
	
	@JsonProperty("guidCotizacion")
	private String guidCotizacion;
	
	@JsonProperty("Owner")
	private String owner;
	
	private List<CrearBeneficiariosDetalleRequest> beneficiarios;
}

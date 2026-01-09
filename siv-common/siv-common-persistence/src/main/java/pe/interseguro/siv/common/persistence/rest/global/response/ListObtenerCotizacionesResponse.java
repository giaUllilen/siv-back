package pe.interseguro.siv.common.persistence.rest.global.response;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import pe.interseguro.siv.common.dto.response.BaseResponseDTO;

@Getter
@Setter
public class ListObtenerCotizacionesResponse implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("respuesta")
	private List<ObtenerCotizacionesResponse> respuesta;
	
}

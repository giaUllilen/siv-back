package pe.interseguro.siv.common.persistence.rest.vidafree.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObtenerCotizacionesResponse {
	private Integer status;
	private String code;
	private String title;
	private String message;
	private List<CotizacionResponse> data; 
}

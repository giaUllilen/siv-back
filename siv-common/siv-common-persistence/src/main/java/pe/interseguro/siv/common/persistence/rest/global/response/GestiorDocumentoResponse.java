package pe.interseguro.siv.common.persistence.rest.global.response;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class GestiorDocumentoResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("data")
	private List<ListGestiorDocumentoResponse> data; 
	
}

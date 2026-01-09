package pe.interseguro.siv.common.persistence.rest.global.response;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrarAfiliacionTraspasoResponse implements Serializable{
	
	private static final long serialVersionUID = 1L;
    
	@JsonProperty("p_retorno")
    private String p_retorno;
    
    @JsonProperty("mensaje")
    private String mensaje;

}

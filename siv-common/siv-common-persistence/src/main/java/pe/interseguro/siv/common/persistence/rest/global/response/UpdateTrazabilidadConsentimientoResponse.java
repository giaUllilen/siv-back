package pe.interseguro.siv.common.persistence.rest.global.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTrazabilidadConsentimientoResponse implements Serializable{
	
	private static final long serialVersionUID = 1L;
    
    private boolean result;
    private String message;

}

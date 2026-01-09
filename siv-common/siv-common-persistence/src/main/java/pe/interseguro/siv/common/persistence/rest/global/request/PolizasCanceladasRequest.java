package pe.interseguro.siv.common.persistence.rest.global.request;
import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PolizasCanceladasRequest implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String numeroDocumento;
}

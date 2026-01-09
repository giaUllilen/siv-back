package pe.interseguro.siv.common.persistence.rest.crm.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetIdOportunidadRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1136795193119415522L;
	
	@JsonProperty("DomainName")
	private String domainName;
	
	@JsonProperty("strTipoDoc")
	private String tipoDocumento;
	
	@JsonProperty("strNroDoc")
	private String numeroDocumento;
}

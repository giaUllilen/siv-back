package pe.interseguro.siv.common.persistence.rest.crm.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidarAccesoRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4632381953740876465L;
	@JsonProperty("DomainName")
	private String domainName;
	
	
}

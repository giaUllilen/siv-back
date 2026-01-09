package pe.interseguro.siv.common.persistence.rest.global.response;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class obtenerPotentialVtigerResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("tipodocumento")
	private String tipodocumento;
	
	@JsonProperty("nrodocumento")
	private String nrodocumento;
	
	@JsonProperty("potentialid")
	private int potentialid;
	
	@JsonProperty("potentialname")
	private String potentialname;
	
	@JsonProperty("amount")
	private String amount;
	
	@JsonProperty("Mensaje")
	private String Mensaje;
	
}

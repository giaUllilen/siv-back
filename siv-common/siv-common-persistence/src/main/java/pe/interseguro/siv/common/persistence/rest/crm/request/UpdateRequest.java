package pe.interseguro.siv.common.persistence.rest.crm.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8159953354457857636L;
	
	private UpdateDetRequest param;

	
}

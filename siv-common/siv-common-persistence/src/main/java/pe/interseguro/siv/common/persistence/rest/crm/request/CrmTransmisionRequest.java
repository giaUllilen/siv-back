package pe.interseguro.siv.common.persistence.rest.crm.request;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrmTransmisionRequest implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1642794992709097884L;
	
	private String idCotizacion;
	private String flag;
	private String idUser;

}


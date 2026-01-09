package pe.interseguro.siv.common.persistence.rest.global.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObtenerAgenteRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String codAgencia;
	/*
	private String COD_USERNAME_AGENTE;
	private String GLS_CORREO_AGENTE;
	private String COD_AGENCIA;
	private String COD_AGENTE;*/
}

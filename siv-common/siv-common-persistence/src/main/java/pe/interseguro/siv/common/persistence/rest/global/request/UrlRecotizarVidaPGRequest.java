package pe.interseguro.siv.common.persistence.rest.global.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UrlRecotizarVidaPGRequest implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String numeroCotizacion;
    String codigoUsuario;
    String codigoAgente;
    Integer fumador;	
    String agente;	

}

package pe.interseguro.siv.common.persistence.rest.crm.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UrlRecotizarVidaRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2394906528395180555L;

	private String strIdcotizacion;
}

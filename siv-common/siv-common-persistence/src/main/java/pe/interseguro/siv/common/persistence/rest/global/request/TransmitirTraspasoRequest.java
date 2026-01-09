package pe.interseguro.siv.common.persistence.rest.global.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransmitirTraspasoRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String propuestaOrigen;
	private String propuestaDestino;
	private String traspasoDigital;
	private String usuario;
}

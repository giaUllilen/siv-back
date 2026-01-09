package pe.interseguro.siv.common.persistence.rest.cotizador.response;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObtenerExigenciaMedicaResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8333138557099185968L;

	private List<ExigenciaMedicaResponse> requerimientos;
}

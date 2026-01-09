package pe.interseguro.siv.common.persistence.rest.vidafree.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsultaCotizacionDetalleResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6268229667216874274L;

	private Integer status;
    private String code;
    private String title;
    private String message;
    private DataCotizacionDetalleResponse data;
}

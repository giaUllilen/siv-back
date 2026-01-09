package pe.interseguro.siv.common.persistence.rest.sitc.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DigitalizacionPagoRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8174984554628091829L;

	private String numeroPropuesta;
	private Double monto;
	private String moneda;
	private Integer idPropuestaAfiliacion;
	private String idCulqui;
	private String usuario;
	private String usuarioAplicacion;
	private String pasarela;
	private String pasarelaPp;
	private String indPagoPasarela;
}

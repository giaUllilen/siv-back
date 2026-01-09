package pe.interseguro.siv.common.dto.request;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)

public class SolicitudFinalizarProcesoRequestDTO extends BaseRequestDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2132011845227334605L;
	
	private Long idSolicitud;
	private String tipoProducto;
	private String usuarioLogin;

}

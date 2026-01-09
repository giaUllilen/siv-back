package pe.interseguro.siv.common.dto.request;

import java.io.Serializable;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.interseguro.siv.common.bean.CotizacionPDFBean;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class CotizadorCorreoRequestDTO implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = -5321537298978188130L;

	private String destinatario;
	List<CotizacionPDFBean> adjuntos;
	String asegurado;
	String agenteNombre;
	String agenteCorreo;
}

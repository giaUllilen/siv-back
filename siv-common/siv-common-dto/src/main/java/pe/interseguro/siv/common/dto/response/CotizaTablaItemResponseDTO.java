package pe.interseguro.siv.common.dto.response;

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
public class CotizaTablaItemResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -981221308999827401L;

	private String codigo;
	private Integer orden;
	private String valor;
	private String valorAuxiliar;
	private String estado;
	
	
}

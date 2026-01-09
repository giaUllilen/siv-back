package pe.interseguro.siv.common.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransmisionAcseleResponseDTO extends BaseResponseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6129315464380072857L;
	
	private String errorCode;
	private String errorMessage;
	private String respuesta;
	private String estadoOperacion;
	private String estadoPoliza;
	private String idOperacion;
	private String idPoliza;
	private String metaPlaft;
	private String recordsPlaft;
}

package pe.interseguro.siv.common.persistence.rest.global.response;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ListObtenerIdPersonaResponse implements Serializable {

	
		private static final long serialVersionUID = 5699070480716869016L;
		
		private List<ObtenerPersonaDocumentoResponse> data;

	
}

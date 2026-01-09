package pe.interseguro.siv.common.persistence.rest.global.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import pe.interseguro.siv.common.persistence.db.acsele.bean.PolizaEstado;

@Getter
@Setter
public class PolizasCanceladasResponse {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("respuesta")
	List<PolizaEstado> respuesta;
}

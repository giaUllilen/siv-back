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
public class CotizacionCrmOportunidadRequestDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5285507090833238963L;
	private String userId;
	private String oportunidadId;
	private String jsonCotizaciones;
}

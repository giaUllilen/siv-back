package pe.interseguro.siv.common.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NiubizFirstPayAntifraudRequestDTO extends BaseRequestDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8447688201270341290L;

	private String clientIp;
}

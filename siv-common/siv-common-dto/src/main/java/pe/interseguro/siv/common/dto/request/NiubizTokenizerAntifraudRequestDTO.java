package pe.interseguro.siv.common.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class NiubizTokenizerAntifraudRequestDTO extends BaseRequestDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7967473405515404009L;
	
	private String clientIp;
}

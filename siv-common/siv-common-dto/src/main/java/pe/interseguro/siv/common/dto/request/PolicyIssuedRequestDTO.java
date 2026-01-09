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
public class PolicyIssuedRequestDTO extends BaseRequestDTO {/**
	 * 
	 */
	private static final long serialVersionUID = 1458344789883254805L;

	
	private Long numeroPropuesta;
}

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
public class NiubizSessionRequestDTO extends BaseRequestDTO {/**
	 * 
	 */
	private static final long serialVersionUID = 5140929388899822684L;

	private String currency;
	private Double amount;
	private String clientIP;
	private String email;
	private String saleNumber;
	private String identityNumber;
	private Double recurrenceMaxAmount;
	
}

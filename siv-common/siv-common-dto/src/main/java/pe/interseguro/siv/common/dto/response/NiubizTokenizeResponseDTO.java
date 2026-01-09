package pe.interseguro.siv.common.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class NiubizTokenizeResponseDTO extends BaseResponseDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 916060927725799497L;
	
	private String customerId;
	private NiubizTokenizerCardResponseDTO card;
	private String message;
	private String providerCode;
	private String providerMessage;
}

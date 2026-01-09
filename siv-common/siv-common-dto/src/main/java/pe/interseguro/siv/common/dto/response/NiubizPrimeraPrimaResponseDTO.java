package pe.interseguro.siv.common.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NiubizPrimeraPrimaResponseDTO extends BaseResponseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2423745841645337256L;
	
	private String subscriptionId;
	private String subscriptionId2;
	private String paymentId;
	private String providerId;
	private String paymentStatus;
	private String cardBrand;
	private String cardNumber;
	private String providerCode;
	private String providerMessage;
	private String token;
	private String tokenAfiliacion;
	
}

package pe.interseguro.siv.common.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NiubizPrimeraPrimaRequestDTO extends BaseRequestDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -318684705317471883L;

	private String token;
	private String currency;
	private NiubizFirstPayCustomerRequestDTO customer;
	private NiubizFirstPaySubscriptionRequestDTO subscription;
	private NiubizFirstPayAntifraudRequestDTO antifraud;
	private String purchaseNumber;
	private String tokenId;
	private String idViaCobroPP;
}

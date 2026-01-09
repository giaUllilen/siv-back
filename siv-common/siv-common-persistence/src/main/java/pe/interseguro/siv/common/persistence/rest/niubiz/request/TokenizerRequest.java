package pe.interseguro.siv.common.persistence.rest.niubiz.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenizerRequest {
	private String tokenId;
	private String currency;
	private TokenizerCustomerRequest customer;
	//private TokenizerAntifraudRequest antifraud;
}

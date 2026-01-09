package pe.interseguro.siv.common.persistence.rest.niubiz.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FirstPayRequest {
	private String token;
	private FirstPayCustomer customer;
	private FirstPaySubscription subscription;
	private String provider;
	//private FirstPayAntifraud antifraud;
	private String tokenId;
	private String email;
	private String currency;
	private double amount;
	private String purchaseNumber;
	private boolean oneShot;
	private FisrtPaySubscriptionMetadataDigital metadataDigitalPayment;
}

package pe.interseguro.siv.common.persistence.rest.niubiz.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FirstPaySubscription {
	private String objectId;
	private String currency;
	private Double amount;
	private FisrtPaySubscriptionMetadata metadata;
	private int years;
	private String frecuency;
	private FisrtPaySubscriptionMetadataAfiliacion metadataAffiliation;
	private FisrtPaySubscriptionMetadataDigital metadataDigitalPayment;
	private String provider;
	private String provider_pp;
	private String applicationUser;
	private String processProviderId;
}

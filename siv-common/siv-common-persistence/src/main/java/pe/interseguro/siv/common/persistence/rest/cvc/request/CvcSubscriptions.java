package pe.interseguro.siv.common.persistence.rest.cvc.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CvcSubscriptions {
	private String objectId;
	private String productName;
	private String currency;
	private Float amount;
	private CvcSubscriptionMetadata metadata;
	private CvcSubscriptionMetadataAfiliacion metadataAffiliation;
}

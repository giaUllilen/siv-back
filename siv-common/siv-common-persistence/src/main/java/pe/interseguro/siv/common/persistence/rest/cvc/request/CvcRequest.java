package pe.interseguro.siv.common.persistence.rest.cvc.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CvcRequest {
	private String token;
	private String ip;
	private String currency;
	private String provider;
	private CvcCustomer customer;
	private List<CvcSubscriptions> subscriptions;
}

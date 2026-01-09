package pe.interseguro.siv.common.persistence.rest.niubiz.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FirstPayDataResponse {
	private String subscriptionId;
	private String objectId;
	private FirstPayPaymentResponse payment;
	private FirstPayCardResponse card;
	private List<FirstPaySuscription> subscriptions;
	private List<FirstPayScheduleResponse> schedule;
	private String id;
}

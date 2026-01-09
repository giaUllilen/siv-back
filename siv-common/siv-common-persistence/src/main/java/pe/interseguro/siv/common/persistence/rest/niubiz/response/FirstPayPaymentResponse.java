package pe.interseguro.siv.common.persistence.rest.niubiz.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FirstPayPaymentResponse {
	private String id;
	private String objectId;
	private String providerId;
	private String status;
}

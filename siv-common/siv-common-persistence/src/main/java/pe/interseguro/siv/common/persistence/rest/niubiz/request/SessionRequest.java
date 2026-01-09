package pe.interseguro.siv.common.persistence.rest.niubiz.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessionRequest {
	private String currency;
	private Double amount;
	private String clientIP;
	private SessionAntifraudMerchant merchantDefineData;
	private Double recurrenceMaxAmount;
	private Boolean oneShot;
}

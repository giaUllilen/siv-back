package pe.interseguro.siv.common.persistence.rest.niubiz.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NiubizSessionRequest {
	private double amount;
	private NiubizSessionAntifraud antifraud;
	private String channel;
	private double recurrenceMaxAmount;
}

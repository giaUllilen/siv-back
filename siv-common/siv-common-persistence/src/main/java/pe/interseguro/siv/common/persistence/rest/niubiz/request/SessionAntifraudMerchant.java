package pe.interseguro.siv.common.persistence.rest.niubiz.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessionAntifraudMerchant implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1884277497083939829L;
	@JsonProperty("MDD4")
	private String MDD4;
	@JsonProperty("MDD21")
	private String MDD21;
	@JsonProperty("MDD30")
	private String MDD30;
	@JsonProperty("MDD32")
	private String MDD32;
	@JsonProperty("MDD40")
	private String MDD40;
	@JsonProperty("MDD42")
	private String MDD42;
	@JsonProperty("MDD75")
	private String MDD75;
	@JsonProperty("MDD77")
	private String MDD77;
}

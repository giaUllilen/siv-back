package pe.interseguro.siv.common.persistence.rest.niubiz.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NiubizSessionAntifraudMerchant {
	@JsonProperty("MDD{Nox}")
	private String mdd0;
	@JsonProperty("MDD{Nox+1}")
	private String mdd1;
	@JsonProperty("MDD{Nox+2}")
	private String mdd2;
}

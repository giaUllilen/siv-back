package pe.interseguro.siv.common.persistence.rest.niubiz.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NiubizSessionAntifraud {
	private String clientIp;
	private NiubizSessionAntifraudMerchant merchantDefineData;
}

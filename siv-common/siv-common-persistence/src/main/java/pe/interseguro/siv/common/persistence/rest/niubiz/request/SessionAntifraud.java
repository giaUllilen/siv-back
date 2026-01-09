package pe.interseguro.siv.common.persistence.rest.niubiz.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessionAntifraud {
	private String clientIp;
	private SessionAntifraudMerchant merchantDefineData;
}

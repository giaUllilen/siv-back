package pe.interseguro.siv.common.dto.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NiubizSessionAntifraudRequestDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1382784992394148356L;
	private String clientIp;
	private NiubizSessionAntifraudMerchantRequestDTO merchantDefineData;
}

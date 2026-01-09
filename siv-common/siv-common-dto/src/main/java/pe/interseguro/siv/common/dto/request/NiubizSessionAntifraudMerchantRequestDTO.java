package pe.interseguro.siv.common.dto.request;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class NiubizSessionAntifraudMerchantRequestDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1778570721759872002L;
	private String MDD1;
	private String MDD2;
	private String MDD3;
	private String MDD4;
}

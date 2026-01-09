package pe.interseguro.siv.common.dto.response;

import pe.interseguro.siv.common.bean.CotizacionTokenBean;

public class TokenResponseDTO extends BaseResponseDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6355550783440084550L;

	private CotizacionTokenBean tokenDecrypt;

	public CotizacionTokenBean getTokenDecrypt() {
		return tokenDecrypt;
	}

	public void setTokenDecrypt(CotizacionTokenBean tokenDecrypt) {
		this.tokenDecrypt = tokenDecrypt;
	}
}

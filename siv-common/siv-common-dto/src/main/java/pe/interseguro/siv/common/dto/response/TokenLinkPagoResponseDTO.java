package pe.interseguro.siv.common.dto.response;

import pe.interseguro.siv.common.bean.PagoTokenBean;

public class TokenLinkPagoResponseDTO extends BaseResponseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8619167729961453249L;

	private PagoTokenBean tokenDecrypt;

	public PagoTokenBean getTokenDecrypt() {
		return tokenDecrypt;
	}

	public void setTokenDecrypt(PagoTokenBean tokenDecrypt) {
		this.tokenDecrypt = tokenDecrypt;
	}
}

package pe.interseguro.siv.common.dto.response;

import java.util.Map;

public class CulqiBaseResponseDTO extends BaseResponseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7718471628047065208L;

	private String culqiHttp;
	private String culqiError;
	private CulqiBaseDetalleResponse culqiResponse;

	public CulqiBaseDetalleResponse getCulqiResponse() {
		return culqiResponse;
	}

	public void setCulqiResponse(CulqiBaseDetalleResponse culqiResponse) {
		this.culqiResponse = culqiResponse;
	}

	public String getCulqiHttp() {
		return culqiHttp;
	}

	public void setCulqiHttp(String culqiHttp) {
		this.culqiHttp = culqiHttp;
	}

	public String getCulqiError() {
		return culqiError;
	}

	public void setCulqiError(String culqiError) {
		this.culqiError = culqiError;
	}
	
}

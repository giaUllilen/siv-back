package pe.interseguro.siv.common.dto.response;

import java.util.List;

public class ProcesoResponseDTO extends BaseResponseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3690282992724235890L;

	private List<String> exitos;
	private List<String> errores;
	
	public List<String> getExitos() {
		return exitos;
	}
	public void setExitos(List<String> exitos) {
		this.exitos = exitos;
	}
	public List<String> getErrores() {
		return errores;
	}
	public void setErrores(List<String> errores) {
		this.errores = errores;
	}
}

package pe.interseguro.siv.common.dto.response;

public class NiubizSessionResponseDTO extends BaseResponseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8290099730589561604L;

	private String sesionID;

	public String getSesionID() {
		return sesionID;
	}

	public void setSesionID(String sesionID) {
		this.sesionID = sesionID;
	}
}

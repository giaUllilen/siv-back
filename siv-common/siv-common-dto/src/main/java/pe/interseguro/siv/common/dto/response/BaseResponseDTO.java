package pe.interseguro.siv.common.dto.response;

import java.io.Serializable;
import java.util.List;

import pe.interseguro.siv.common.exception.ErrorResourceDTO;

/**
 * Clase DTO Base para las respuestas (response) al REST API 
 *  
 * @author ti-is
 */
public class BaseResponseDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7393543723967936508L;
	
	private String codigoRespuesta;
	private String mensajeRespuesta;
	private String observaciones;
	private ErrorResourceDTO objErrorResource;
	
	
	public String getCodigoRespuesta() {
		return codigoRespuesta;
	}
	public void setCodigoRespuesta(String codigoRespuesta) {
		this.codigoRespuesta = codigoRespuesta;
	}
	public String getMensajeRespuesta() {
		return mensajeRespuesta;
	}
	public void setMensajeRespuesta(String mensajeRespuesta) {
		this.mensajeRespuesta = mensajeRespuesta;
	}
	public ErrorResourceDTO getObjErrorResource() {
		return objErrorResource;
	}
	public void setObjErrorResource(ErrorResourceDTO objErrorResource) {
		this.objErrorResource = objErrorResource;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

}
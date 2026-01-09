
package pe.interseguro.siv.common.exception;

/**
 * Clase Exception base para capturar atributos relevantes de la excepción
 * 
 * @author ti-is
 */
public class SivBaseException extends RuntimeException {

	private static final long serialVersionUID = -6202381788386413399L;
	
	private Exception objException;
	private String codError;
	private String msjError;

	
	public SivBaseException() {
		super();
	}

	public SivBaseException(Exception objException) {
		this.objException = objException;
	}

	public SivBaseException(String msjError) {
//		super(msjError);
		this.msjError = msjError;
	}

	public SivBaseException(String msjError, Exception objException) {
		super(objException);
		this.objException = objException;
		this.msjError = msjError;
	}

	public SivBaseException(String codError, String msjError, Exception objException) {
		super(msjError);
		this.codError = codError;
		this.msjError = msjError;
		this.objException = objException;
	}

	public Exception getObjException() {
		return objException;
	}

	public void setObjException(Exception objException) {
		this.objException = objException;
	}

	public String getCodError() {
		return codError;
	}

	public void setCodError(String codError) {
		this.codError = codError;
	}

	public String getMsjError() {
		return msjError;
	}

	public void setMsjError(String msjError) {
		this.msjError = msjError;
	}

	public String getMessage() {
		return objException != null ? objException.getMessage() : null;
	}
	

}

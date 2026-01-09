package pe.interseguro.siv.common.persistence.rest.indenova.response;

import java.io.Serializable;
import java.nio.file.Path;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DocumentoCircuitoResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 357578457439893618L;
	
	//private byte[] documento;
	private String fileZip;
	private String filePdf;
	private Path filePdfPath;
	private String filePdfName;
	private boolean error;
	private String errorMessage;
}

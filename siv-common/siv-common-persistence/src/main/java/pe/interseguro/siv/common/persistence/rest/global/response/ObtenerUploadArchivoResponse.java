package pe.interseguro.siv.common.persistence.rest.global.response;


import java.util.List;


import lombok.Getter;
import lombok.Setter;
import pe.interseguro.siv.common.persistence.rest.niubiz.response.BaseResponse;

@Getter
@Setter
public class ObtenerUploadArchivoResponse extends BaseResponse{
	
	/**
	 * 
	 */
	
	private List<ArchivoResponse> files;
	
	private String contactId;
	

}

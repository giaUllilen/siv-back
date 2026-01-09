package pe.interseguro.siv.common.persistence.rest.global.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObtenerMetadataPublishRequest implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String applicationUser;
	private String origin;
	private String poliza;
	private String propuesta;
	private String fechaOperacion;
	private String tipoOperacion;

}

package pe.interseguro.siv.common.persistence.rest.crm.response;

import lombok.Getter;
import lombok.Setter;
import pe.interseguro.siv.common.persistence.rest.global.response.UrlResponse;
import pe.interseguro.siv.common.persistence.rest.niubiz.response.BaseResponse;
@Getter
@Setter
public class UrlArchivoResponse extends BaseResponse {
	/**
	 * 
	 */
	
	private UrlResponse url;
	
	/*
	 * @JsonProperty("url")
	private String url;*/
}

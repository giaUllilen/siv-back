package pe.interseguro.siv.common.persistence.rest.plaft.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import pe.interseguro.siv.common.persistence.rest.interseguro.request.DigitalTokenDetResponse;
import pe.interseguro.siv.common.persistence.rest.niubiz.response.BaseResponse;

@Getter
@Setter
public class PlaftTokenResponse extends BaseResponse{
	

//	@JsonProperty("success")
//	private String success;
	
//	@JsonProperty("mensaje")
//	private String mensaje;
	
//	@JsonProperty("extra")
//	private String extra;
	
	@JsonProperty("_meta")
	private PlaftTokenMetaResponse _meta;
	
	@JsonProperty("records")
	private PlaftTokenDetResponse records;
	
//	@JsonProperty("items")
//	private String items;
	
}

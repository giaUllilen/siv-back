package pe.interseguro.siv.common.persistence.rest.global.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadResponse {

	@JsonProperty("Mensaje")
	private String Mensaje;
}

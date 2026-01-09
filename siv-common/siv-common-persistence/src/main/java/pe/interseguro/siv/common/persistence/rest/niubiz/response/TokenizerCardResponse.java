package pe.interseguro.siv.common.persistence.rest.niubiz.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenizerCardResponse {
	 private String id;
	 private String creationToken;
	 private String resourceId;
	 private String number;
	 private String brand;
	 private String type;
	 private String category;
	 private Boolean active;
}

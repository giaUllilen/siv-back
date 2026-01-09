package pe.interseguro.siv.common.persistence.rest.niubiz.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FirstPayCardResponse {
	private String type;
	private String number;
	private String brand;
	private String category;
	private String tokenizedId;
	private String creationToken;
}

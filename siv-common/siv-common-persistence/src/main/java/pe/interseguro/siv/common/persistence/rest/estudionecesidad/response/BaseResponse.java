package pe.interseguro.siv.common.persistence.rest.estudionecesidad.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseResponse {
	private String statusHttp;
	private String code;
	private String title;
	private String message;
	private String messageUser;
}

package pe.interseguro.siv.common.persistence.rest.interseguro.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnviarCorreoResponseNotify {

	private String idCreated;
	private String message;
	private String status;

}

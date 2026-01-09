package pe.interseguro.siv.common.dto.response;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class LogResponseDTO implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = -6191568580961235773L;

	private int id;
	private String screen;
	private String path;
	private String code;
	private String message;
	private String error;
	private String comment;
	private String agent;
	private String device;
	private String os;
	private String time;
}

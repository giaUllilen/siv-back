package pe.interseguro.siv.common.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class TabletAuditoriaRequestDTO extends BaseRequestDTO {/**
	 * 
	 */
	private static final long serialVersionUID = 5783811576957470275L;

	private String modelo;
	private String plataforma;
	private String uuid;
	private String version;
	private String serial;
	private String ip;
	private String captura;
	private String time;
}

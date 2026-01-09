package pe.interseguro.siv.common.dto.request;

import java.util.Date;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class TransmisionAcseleRequestDTO extends BaseRequestDTO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2757492235333650978L;
	
	private String idCotizacion;
	private String flag;
	private String idUser;
	private Long idSolicitud;
	private String usuarioLogin;
}

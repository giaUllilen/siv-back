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
public class ClonarCVFResponseDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1492694408195093805L;
	
	private String err;
	private String id;
	private String msg;
	private String rs;
	private String idCotizacionCRM;
}

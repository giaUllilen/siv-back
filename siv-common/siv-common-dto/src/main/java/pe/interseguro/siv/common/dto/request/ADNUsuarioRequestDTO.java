package pe.interseguro.siv.common.dto.request;

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
public class ADNUsuarioRequestDTO implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = -4549028665477496266L;

	private String idUsuario;
}

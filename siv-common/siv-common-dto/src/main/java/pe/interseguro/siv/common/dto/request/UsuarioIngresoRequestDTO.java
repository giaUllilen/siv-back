package pe.interseguro.siv.common.dto.request;

import javax.validation.constraints.NotBlank;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class UsuarioIngresoRequestDTO extends BaseRequestDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3791377896877862430L;

	@NotBlank
	private String usuario;

	@NotBlank
	private String password;

	
}

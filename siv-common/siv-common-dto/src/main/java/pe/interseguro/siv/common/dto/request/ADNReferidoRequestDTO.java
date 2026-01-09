package pe.interseguro.siv.common.dto.request;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ADNReferidoRequestDTO implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = -8698362848012288029L;

	@NotBlank
	private String nombres;
	
	@NotNull
	private String telefono;
}

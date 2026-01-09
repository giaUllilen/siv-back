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
public class ADNClienteRequestDTO implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = -7989014275175495340L;
	
	private String numeroDocumento;
}

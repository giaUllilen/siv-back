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
public class ADNReglamentoReferidoResponseDTO implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9129664227894298007L;
	
	private String nombres;
	private String telefono;
	
}

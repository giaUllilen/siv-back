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
public class SolicitudFormularioCoberturaResponseDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 350320433868958302L;
	private String idCobertura;
	private String tipoCobertura;
	private String nombre;
	private Float capitalAsegurado;
	private Float primaAnual;

}

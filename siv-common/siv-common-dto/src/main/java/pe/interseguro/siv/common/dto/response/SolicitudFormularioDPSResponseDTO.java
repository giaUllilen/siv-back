package pe.interseguro.siv.common.dto.response;

import java.io.Serializable;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)

public class SolicitudFormularioDPSResponseDTO implements Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -8556967883952312170L;
	
	private String estatura;
    private String peso;
    private String imc;
    private String pesoVariacion;
    private String pesoAumentoDisminuyo;
    private String pesoCantidad;
    private String pesoMotivo;
    private String fumador;
    private String fumadorCantidad;
    private String fumadorFrecuencia;
    private String drogas;
    private String drogasFecha;
    private String alcohol;
    private String alcoholCantidad;
    private String alcoholFrecuencia;
    private List<SolicitudFormularioDPSPreguntaResponseDTO> preguntas;

}

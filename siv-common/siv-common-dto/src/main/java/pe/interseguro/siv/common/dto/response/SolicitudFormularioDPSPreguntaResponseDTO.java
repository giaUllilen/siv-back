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

public class SolicitudFormularioDPSPreguntaResponseDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6335081253981631110L;
	
	private String item;
    private String pregunta;
    private String respuesta;
    private String detalle;
    private String enfermedad;
    private String fechaDiagnostico;
    private String condicionActual;
    private String nombreMedicoHospital;

}

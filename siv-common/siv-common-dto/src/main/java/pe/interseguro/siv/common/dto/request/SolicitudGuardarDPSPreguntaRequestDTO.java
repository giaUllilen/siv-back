package pe.interseguro.siv.common.dto.request;

import java.io.Serializable;
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
public class SolicitudGuardarDPSPreguntaRequestDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5363882130482927826L;
	
	private String item;
    private String pregunta;
    private String respuesta;
    private String detalle;
    private String enfermedad;
    private String fechaDiagnostico;
    private String condicionActual;
    private String nombreMedicoHospital;

}

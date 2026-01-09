package pe.interseguro.siv.common.bean;

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
public class SolicitudPDFDPSPreguntaBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5496958837727521405L;
	
	private String bloquePregunta; //1  o 2
	private String item; //1 al 7 y de a al e
	private String pregunta; //numero del 1 al 12
	private boolean checkSi;
	private boolean checkNo;
	private String detalle;
	private String enfermedad;
	private String fechaDiagnostico;
	private String condicionActual;
	private String nombreMedicoHospital;

}

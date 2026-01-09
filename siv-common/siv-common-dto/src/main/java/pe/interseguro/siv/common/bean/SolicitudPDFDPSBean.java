package pe.interseguro.siv.common.bean;

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
public class SolicitudPDFDPSBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8127224727404743659L;
	
	private String estatura;
	private String peso;
	private String imc;
	private boolean checkVariacionSi;
	private boolean checkVariacionNo;
	private boolean checkPesoAumento;
	private boolean checkPesoDisminuyo;
	private String pesoVariacionCantidad;
	private String pesoMotivo;
	private boolean checkFumadorSi;
	private boolean checkFumadorNo;
	private String fumadorCantidad;
	private String fumadorFrecuencia;
	private boolean checkConsumeDrogasSi;
	private boolean checkConsumeDrogasNo;
	private String drogasFecha;
	private boolean checkAlchoolSi;
	private boolean checkAlchoolNo;
	private String alcoholCantidad;
	private String alcoholFrecuencia;
	private List<SolicitudPDFDPSPreguntaBean> preguntas;

}

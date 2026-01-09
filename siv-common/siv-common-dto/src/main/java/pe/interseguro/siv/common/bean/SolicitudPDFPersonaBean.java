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
public class SolicitudPDFPersonaBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4311222761661904702L;
	
	private String apellidoPaterno;
	private String apellidoMaterno;
	private String nombres;
	private String nombresCompletos;
	private String razonSocial;
	private String nacimientoAnio1;
	private String nacimientoAnio2;
	private String nacimientoAnio3;
	private String nacimientoAnio4;
	private String nacimientoMes1;
	private String nacimientoMes2;
	private String nacimientoDia1;
	private String nacimientoDia2;
	private boolean checkDocumentoDNI;
	private boolean checkDocumentoRUC;
	private boolean checkDocumentoCE;
	private String numeroDocumento;
	private boolean checkSexoMasculino;
	private boolean checkSexoFemenino;
	private boolean checkEstadoCivilSotero;
	private boolean checkEstadoCivilCasado;
	private boolean checkEstadoCivilViudo;
	private boolean checkEstadoCivilDivorciado;
	private boolean checkEstadoCivilConviviente;
	private String nacionalidad;
	private boolean checkDireccionJR;
	private boolean checkDireccionAV;
	private boolean checkDireccionCA;
	private boolean checkDireccionPJ;
	private String direccion;
	private String celular;
	private String email;
	private boolean checkPEPSi;
	private boolean checkPEPNo;
	private boolean checkSujetoObligadoSi;
	private boolean checkSujetoObligadoNo;
	private String centroTrabajo;
	private String actividadEconomica;
	private boolean checkIngresoSoles;
	private boolean checkIngresoDolares;
	private String ingresoMensual;
	private String profesion;
	private String direccionLaboral;
	private boolean checkFumadorSi;
	private boolean checkFumadorNo;
}


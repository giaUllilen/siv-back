package pe.interseguro.siv.common.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class AcpPDFPersonaBean implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -4933348293042811271L;

	@JsonProperty("apellido_paterno")
	private String apellidoPaterno;
	@JsonProperty("apellido_materno")
	private String apellidoMaterno;
	@JsonProperty("nombres")
	private String nombres;
	@JsonProperty("nombres_completos")
	private String nombresCompletos;
	@JsonProperty("razon_social")
	private String razonSocial;
	@JsonProperty("checkDocumentoDNI")
	private boolean checkDocumentoDNI;
	@JsonProperty("checkDocumentoRUC")
	private boolean checkDocumentoRUC;
	@JsonProperty("checkDocumentoCE")
	private boolean checkDocumentoCE;
	@JsonProperty("checkDocumentoCI")
	private boolean checkDocumentoCI;
	@JsonProperty("checkDocumentoOtro")
	private boolean checkDocumentoOtro;
	@JsonProperty("numero_documento")
	private String numeroDocumento;
	@JsonProperty("checkDireccionJR")
	private boolean checkDireccionJR;
	@JsonProperty("checkDireccionAV")
	private boolean checkDireccionAV;
	@JsonProperty("checkDireccionCA")
	private boolean checkDireccionCA;
	@JsonProperty("checkDireccionOtro")
	private boolean checkDireccionOtro;
	@JsonProperty("direccion")
	private String direccion;
	@JsonProperty("nro_mz_lt")
	private String nroMzLt;
	@JsonProperty("dpto_of_int")
	private String dptoOfInt;
	@JsonProperty("distrito")
	private String distrito;
	@JsonProperty("provincia")
	private String provincia;
	@JsonProperty("departamento")
	private String departamento;
	@JsonProperty("celular")
	private String celular;
	@JsonProperty("checkFrecuenciaAnual")
	private boolean checkFrecuenciaAnual;
	@JsonProperty("checkFrecuenciaSemestral")
	private boolean checkFrecuenciaSemestral;
	@JsonProperty("checkFrecuenciaTrimestral")
	private boolean checkFrecuenciaTrimestral;
	@JsonProperty("checkFrecuenciaMensual")
	private boolean checkFrecuenciaMensual;
	@JsonProperty("correo_electronico")
	private String email;
}

package pe.interseguro.siv.common.persistence.rest.crm.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCotizacionParamRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -439166649666041169L;


	@JsonProperty("CotizacionId")
	private String cotizacionId;

	@JsonProperty("inter_consentimientocomelectronica")
	private String consentimientoComElectronica;

	@JsonProperty("inter_fuentecomelectronica")
	private String fuenteComElectronica;

	@JsonProperty("inter_fechaconsentimientoelectronica")
	private String fechaConsentimientoElectronica;

	@JsonProperty("inter_estado")
	private String estado;

	@JsonProperty("inter_fechafirmadocumentos")
	private String fechaFirmaDocumentos;

	@JsonProperty("inter_contratanteigualasegurado")
	private String contratanteIgualAsegurado;

	@JsonProperty("inter_emailasegurado")
	private String emailAsegurado;

	@JsonProperty("inter_tipodedocumento")
	private String tipoDocumento;

	@JsonProperty("inter_numerodocumento")
	private String numeroDocumento;

	@JsonProperty("inter_nombre")
	private String nombre;

	@JsonProperty("inter_apellidopaterno")
	private String apellidoPaterno;

	@JsonProperty("inter_apellidomaterno")
	private String apellidoMaterno;

	@JsonProperty("inter_sexo")
	private String sexo;

	@JsonProperty("inter_estadocivil")
	private String estadoCivil;

	@JsonProperty("inter_fechadenacimiento")
	private String fechaNacimiento;

	@JsonProperty("inter_fumador")
	private String fumador;

	@JsonProperty("inter_condicionsujetoobligado")
	private String condicionSujetObligado;

	@JsonProperty("inter_indicadorpep")
	private String indicadorPep;

	@JsonProperty("inter_telefonofijo")
	private String telefonoFijo;

	@JsonProperty("inter_telefonocelular")
	private String telefonoCelular;

	@JsonProperty("inter_telefonotrabajo")
	private String telefonoTrabajo;

	@JsonProperty("inter_correoelectronico")
	private String correoElectronico;

	@JsonProperty("inter_profesion")
	private String profesion;

	@JsonProperty("inter_profesioncotizadorcontratante")
	private String profesionCotizadorContratante;

	@JsonProperty("inter_actividadeconomica")
	private String actividadEconomica;

	@JsonProperty("inter_actividadeconomicaacsele")
	private String actividadEconomicaAcsele;

	@JsonProperty("inter_centrolaboral")
	private String centroLaboral;

	@JsonProperty("inter_nacionalidad")
	private String nacionalidad;

	@JsonProperty("inter_relacionasegurable")
	private String relacionAsegurable;

	@JsonProperty("inter_tipodireccioncorrespondencia")
	private String tipoDireccionCorrespondencia;

	@JsonProperty("inter_direccionparticular")
	private String direccionParticular;

	@JsonProperty("inter_departamentoparticular")
	private String departamentoParticular;

	@JsonProperty("inter_provinciaparticular")
	private String provinciaParticular;

	@JsonProperty("inter_distritoparticular")
	private String distritoParticular;

	@JsonProperty("inter_direccioncomercial")
	private String direccionComercial;

	@JsonProperty("inter_departamentocomercial")
	private String departamentoComercial;

	@JsonProperty("inter_provinciacomercial")
	private String provinciaComercial;

	@JsonProperty("inter_distritocomercial")
	private String distritoComercial;

	@JsonProperty("inter_resultadodeauditoria")
	private String resultadoAuditoria;

	@JsonProperty("inter_numeropropuesta")
	private String numeroPropuesta;

	@JsonProperty("inter_confirmarnumeropropuesta")
	private String confirmarNumeropropuesta;

	@JsonProperty("inter_envioestadodecuenta")
	private String envioEstadodecuenta;

	@JsonProperty("inter_diadecobro")
	private String diaCobro;
	
	@JsonProperty("inter_origenadndigital")
	private String origenAdnDigital;
	
}

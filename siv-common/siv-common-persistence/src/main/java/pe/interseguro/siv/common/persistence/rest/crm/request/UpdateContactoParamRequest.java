package pe.interseguro.siv.common.persistence.rest.crm.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateContactoParamRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3896275148336103464L;

	@JsonProperty("ContactId")
	private String contactId;

	@JsonProperty("inter_consentimientoasesoria")
	private String consentimientoAsesoria;

	@JsonProperty("inter_fuenteconsentimientoasesoria")
	private String fuenteConsentimientoAsesoria;

	@JsonProperty("inter_fechaconsentimientoasesoria")
	private String fechaConsentimientoAsesoria;

	@JsonProperty("inter_validacionconsentimientoasesoria")
	private String validacionConsentimientoAsesoria;

	@JsonProperty("inter_consentimientotratamientovi")
	private String consentimientoTratamiento;

	@JsonProperty("inter_fuentetratamientovi")
	private String fuenteTratamiento;

	@JsonProperty("inter_fechatratamientovi")
	private String fechaTratamiento;

	@JsonProperty("inter_consentimientotransferenciavi")
	private String consentimientoTransferencia;

	@JsonProperty("inter_fuentetransferenciavi")
	private String fuenteTransferencia;

	@JsonProperty("inter_fechatransferenciavi")
	private String fechaTransferencia;

	@JsonProperty("inter_consentimientocomercializacion")
	private String consentimientoComercializacion;

	@JsonProperty("inter_fuentecomercializacion")
	private String fuenteComercializacion;

	@JsonProperty("inter_fechaconsentimientocomercializacion")
	private String fechaConsentimientoComercializacion;

	@JsonProperty("inter_validacionconsentimiento")
	private String validacionConsentimiento;

	@JsonProperty("inter_telefonoprincipal")
	private String telefonoPrincipal;

	@JsonProperty("inter_telefonofijo")
	private String telefonoFijo;

	@JsonProperty("telephone1")
	private String telephone1;

	@JsonProperty("mobilephone")
	private String celular;

	@JsonProperty("inter_telefonocelulartrabajovida")
	private String telefonoCelularTrabajo;

	@JsonProperty("emailaddress1")
	private String correo;

	@JsonProperty("inter_tipodecliente")
	private String tipoCliente;

	@JsonProperty("inter_tipodedocumento")
	private String tipoDocumento;

	@JsonProperty("inter_numerodedocumento")
	private String numeroDocumento;

	@JsonProperty("inter_estadocivil")
	private String estadoCivil;

	@JsonProperty("inter_nacionalidad")
	private String nacionalidad;

	@JsonProperty("birthdate")
	private String fechaNacimiento;

	@JsonProperty("inter_profesion")
	private String profesion;

	@JsonProperty("inter_profesioncotizador")
	private String profesionCotizador;

	@JsonProperty("transactioncurrencyid")
	private String ingresoMoneda;

	@JsonProperty("annualincome")
	private String ingresoAnual;

	@JsonProperty("inter_centrolaboral")
	private String centroLaboral;

	@JsonProperty("jobtitle")
	private String jobtitle;

	@JsonProperty("inter_origen")
	private String origen;

	@JsonProperty("inter_actividadeconomica")
	private String actividadEconomica;

	@JsonProperty("inter_actividadeconomicaacsele")
	private String actividadEconomicaAcsele;

	@JsonProperty("inter_fumador")
	private String fumador;

	@JsonProperty("inter_condicionsujetoobligado")
	private String sujetoObligado;

	@JsonProperty("inter_fechacontacto")
	private String fechaContacto;

	@JsonProperty("inter_indicadorpep")
	private String indicadorPep;

	@JsonProperty("inter_resultadodeauditoria")
	private String resultadoAuditoria;

	@JsonProperty("inter_tipodireccioncorrespondencia")
	private String tipoDireccion;

	@JsonProperty("address2_line1")
	private String direccion1;

	@JsonProperty("inter_departamentoparticular")
	private String departamento;

	@JsonProperty("inter_provinciaparticular")
	private String provincia;

	@JsonProperty("inter_distritoparticular")
	private String distrito;

	@JsonProperty("address2_line2")
	private String direccion2;

	@JsonProperty("inter_departamentocomercial")
	private String departamentoComercial;

	@JsonProperty("inter_provinciacomercial")
	private String provinciaComercial;

	@JsonProperty("inter_distritocomercial")
	private String distritoComercial;


}

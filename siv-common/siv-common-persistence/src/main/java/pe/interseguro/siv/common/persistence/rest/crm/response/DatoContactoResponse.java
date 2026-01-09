package pe.interseguro.siv.common.persistence.rest.crm.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DatoContactoResponse implements Serializable {

	@JsonProperty("DomainName")
	private String domainName;

	@JsonProperty("actividad_economica")
	private String actividadEconomica;

	@JsonProperty("apellido_materno")
	private String apellidoMaterno;

	@JsonProperty("apellido_paterno")
	private String apellidoPaterno;

	@JsonProperty("cod_actividad_economica")
	private String codigoActividadEconomica;

	@JsonProperty("cod_fumador")
	private String codigoFumador;

	@JsonProperty("cod_profesion")
	private String codigoProfesion;

	@JsonProperty("cod_sexo")
	private String codigoSexo;

	@JsonProperty("cod_tipo_documento")
	private String codigoTipoDocumento;

	@JsonProperty("codigo_agente")
	private String codigoAgente;

	private String email;

	@JsonProperty("fecha_nacimiento")
	private String fechaNacimiento;

	private String fumador;

	private String nombres;

	@JsonProperty("numero_documento")
	private String numeroDocumento;

	private String profesion;

	private String sexo;

	@JsonProperty("tel_celular")
	private String telefonoCelular;

	@JsonProperty("tipo_documento")
	private String tipoDocumento;

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getActividadEconomica() {
		return actividadEconomica;
	}

	public void setActividadEconomica(String actividadEconomica) {
		this.actividadEconomica = actividadEconomica;
	}

	public String getApellidoMaterno() {
		return apellidoMaterno;
	}

	public void setApellidoMaterno(String apellidoMaterno) {
		this.apellidoMaterno = apellidoMaterno;
	}

	public String getApellidoPaterno() {
		return apellidoPaterno;
	}

	public void setApellidoPaterno(String apellidoPaterno) {
		this.apellidoPaterno = apellidoPaterno;
	}

	public String getCodigoActividadEconomica() {
		return codigoActividadEconomica;
	}

	public void setCodigoActividadEconomica(String codigoActividadEconomica) {
		this.codigoActividadEconomica = codigoActividadEconomica;
	}

	public String getCodigoFumador() {
		return codigoFumador;
	}

	public void setCodigoFumador(String codigoFumador) {
		this.codigoFumador = codigoFumador;
	}

	public String getCodigoProfesion() {
		return codigoProfesion;
	}

	public void setCodigoProfesion(String codigoProfesion) {
		this.codigoProfesion = codigoProfesion;
	}

	public String getCodigoSexo() {
		return codigoSexo;
	}

	public void setCodigoSexo(String codigoSexo) {
		this.codigoSexo = codigoSexo;
	}

	public String getCodigoTipoDocumento() {
		return codigoTipoDocumento;
	}

	public void setCodigoTipoDocumento(String codigoTipoDocumento) {
		this.codigoTipoDocumento = codigoTipoDocumento;
	}

	public String getCodigoAgente() {
		return codigoAgente;
	}

	public void setCodigoAgente(String codigoAgente) {
		this.codigoAgente = codigoAgente;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getFumador() {
		return fumador;
	}

	public void setFumador(String fumador) {
		this.fumador = fumador;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public String getProfesion() {
		return profesion;
	}

	public void setProfesion(String profesion) {
		this.profesion = profesion;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getTelefonoCelular() {
		return telefonoCelular;
	}

	public void setTelefonoCelular(String telefonoCelular) {
		this.telefonoCelular = telefonoCelular;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	
}

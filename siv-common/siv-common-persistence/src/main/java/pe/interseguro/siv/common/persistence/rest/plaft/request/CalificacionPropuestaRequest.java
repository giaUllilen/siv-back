package pe.interseguro.siv.common.persistence.rest.plaft.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalificacionPropuestaRequest implements Serializable{
	
//	@JsonProperty("client_id")
	private String client_id;
	
//	@JsonProperty("propuesta")
	private String propuesta;

//	@JsonProperty("prima_anualizada")
	private String prima_anualizada;

//	@JsonProperty("producto")
	private String producto;
	
//	@JsonProperty("moneda")
	private String moneda;
	
//	@JsonProperty("contratante_tipo_documento")
	private String contratante_tipo_documento;
	
//	@JsonProperty("contratante_documento")
	private String contratante_documento;
	
//	@JsonProperty("contratante_actividad_economica")
	private String contratante_actividad_economica;
	
//	@JsonProperty("contratante_profesion")
	private String contratante_profesion;
	
//	@JsonProperty("contratante_sujeto_obligado")
	private String contratante_sujeto_obligado;
	
//	@JsonProperty("contratante_nacionalidad_residencia")
	private String contratante_nacionalidad_residencia;
	
//	@JsonProperty("contratante_pep")
	private String contratante_pep;
	
//	@JsonProperty("contratante_nombre1")
	private String contratante_nombre1;
	
//	@JsonProperty("contratante_nombre2")
	private String contratante_nombre2;
	
//	@JsonProperty("contratante_nombre3")
	private String contratante_nombre3;
	
//	@JsonProperty("contratante_apellido_paterno")
	private String contratante_apellido_paterno;
	
//	@JsonProperty("contratante_apellido_materno")
	private String contratante_apellido_materno;
	
//	@JsonProperty("contratante_razon_social")
	private String contratante_razon_social;
	
//	@JsonProperty("asegurado_tipo_documento")
	private String asegurado_tipo_documento;
	
//	@JsonProperty("asegurado_documento")
	private String asegurado_documento;
	
//	@JsonProperty("asegurado_actividad_economica")
	private String asegurado_actividad_economica;
	
//	@JsonProperty("asegurado_profesion")
	private String asegurado_profesion;
	
//	@JsonProperty("asegurado_sujeto_obligado")
	private String asegurado_sujeto_obligado;
	
//	@JsonProperty("asegurado_nacionalidad_residencia")
	private String asegurado_nacionalidad_residencia;
	
//	@JsonProperty("asegurado_pep")
	private String asegurado_pep;
	
//	@JsonProperty("asegurado_nombre1")
	private String asegurado_nombre1;
	
//	@JsonProperty("asegurado_nombre2")
	private String asegurado_nombre2;
	
//	@JsonProperty("asegurado_nombre3")
	private String asegurado_nombre3;
	
//	@JsonProperty("asegurado_apellido_paterno")
	private String asegurado_apellido_paterno;
	
//	@JsonProperty("asegurado_apellido_materno")
	private String asegurado_apellido_materno;
	
//	@JsonProperty("asegurado_razon_social")
	private String asegurado_razon_social;
	
//	@JsonProperty("contratante_residencia")
	private String contratante_residencia;
	
//	@JsonProperty("asegurado_residencia")
	private String asegurado_residencia;
	
}

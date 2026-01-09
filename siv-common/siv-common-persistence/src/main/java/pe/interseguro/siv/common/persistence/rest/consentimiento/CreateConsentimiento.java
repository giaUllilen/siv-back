package pe.interseguro.siv.common.persistence.rest.consentimiento;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateConsentimiento implements Serializable {
	private Integer id_consentimiento_asesoria;
	private Integer id_configuracion;
	private String cod_tipo_identificacion;
	private String gls_num_identificacion;
	private String gls_nombres;
	private String gls_apellido_paterno;
	private String gls_apellido_materno;
	private String gls_sexo;
	private String fec_nacimiento;
	private String gls_mail;
	private String gls_telefono;
	private String gls_celular;
	private String gls_url_exito;
	private String gls_nombres_agente;
	private String gls_mail_agente;
	private Integer num_agente;
	private String aud_usr_ingreso;
	private ConsentimientoDireccion direccion;
	private String ind_consentimiento;
	private String gls_token;
}


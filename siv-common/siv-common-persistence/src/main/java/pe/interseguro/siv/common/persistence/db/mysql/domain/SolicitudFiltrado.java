package pe.interseguro.siv.common.persistence.db.mysql.domain;

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
public class SolicitudFiltrado implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String tipoDocumento;
	private String numeroDocumento;
	private String estado;
	private String nombres;
	@JsonProperty("apellido_paterno")
	private String apellidoPaterno;
	@JsonProperty("apellido_materno")
	private String apellidoMaterno;
	private String correo;
	private String razonSocial;

}

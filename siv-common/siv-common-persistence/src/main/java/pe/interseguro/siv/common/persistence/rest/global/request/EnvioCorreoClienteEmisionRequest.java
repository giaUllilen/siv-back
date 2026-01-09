package pe.interseguro.siv.common.persistence.rest.global.request;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class EnvioCorreoClienteEmisionRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	private Boolean flagCliente;
	private String nroSolicitud;
	private String nombre;
	private String correo;
}

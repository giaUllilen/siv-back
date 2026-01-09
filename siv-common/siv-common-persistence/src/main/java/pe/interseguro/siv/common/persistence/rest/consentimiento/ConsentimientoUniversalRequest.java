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
public class ConsentimientoUniversalRequest implements Serializable {
	private String tipoDocumento;
	private String numeroDocumento;
	private String usuario;
	private Integer idConfiguracion;
}

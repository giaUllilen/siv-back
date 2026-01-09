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
public class ConsentimientoDireccion implements Serializable{
	private String gls_direccion;
	private Integer cod_tipo_via;
	private String gls_espacio_urbano;
	private String cod_departamento;
	private String cod_provincia;
	private String cod_distrito;
}

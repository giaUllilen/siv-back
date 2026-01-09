package pe.interseguro.siv.common.persistence.db.postgres.bean;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Asegurable {
	private String codigoCliente;
	private String nombres;
	private String apellidoPaterno;
	private String apellidoMaterno;
	private String numeroDocumento;
	private String tipoDocumento;
	private String profesion;
	private String flagIgv;
	private String fechaNacimiento;
	private String sexo;
	private String fumador;
}

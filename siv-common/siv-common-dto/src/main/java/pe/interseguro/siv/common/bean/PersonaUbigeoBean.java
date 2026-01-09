package pe.interseguro.siv.common.bean;




import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonaUbigeoBean implements Serializable {
	private static final long serialVersionUID = -3462326650631223170L;
	private String numeroDocumento;
	private String departamento;
	private String provincia;
	private String distrito;
}

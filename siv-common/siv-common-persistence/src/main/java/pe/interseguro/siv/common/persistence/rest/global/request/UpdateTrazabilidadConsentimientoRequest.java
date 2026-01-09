package pe.interseguro.siv.common.persistence.rest.global.request;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTrazabilidadConsentimientoRequest implements Serializable {
	private String tipoDocumento;
	private String numeroDocumento;
	private String canal; // wadn, sadn, madn
    private String fechaConsentimiento;
}
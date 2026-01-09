package pe.interseguro.siv.common.dto.request;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class CotizadorCrmPlanFinanciamientoDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9026245619906955215L;
	private String idPFConf;
    private String nombrePlanFinanciamiento;
    private String moneda;
}

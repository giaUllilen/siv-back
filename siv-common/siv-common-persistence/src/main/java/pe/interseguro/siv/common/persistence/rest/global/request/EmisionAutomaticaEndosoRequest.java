package pe.interseguro.siv.common.persistence.rest.global.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class EmisionAutomaticaEndosoRequest  implements Serializable {

private static final long serialVersionUID = 1L;
	
	
	private int idProducto;
	private String interesado;
	private String policyNumber;
	private String FechaSolicitud;
	private String ResponsableAtencion;
	private String TipoEndoso;
	private String InicioVigenciaEndoso;
	private String TipoVigenciaEndoso;
	private String Endoso;
	private String FechaMovimiento;
	private String FechaCliente;
	private String FechaEfectiva;
	private String PlanVida;
}

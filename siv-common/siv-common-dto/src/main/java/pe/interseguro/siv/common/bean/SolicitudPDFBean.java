package pe.interseguro.siv.common.bean;

import java.io.Serializable;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class SolicitudPDFBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1870090329647364416L;
	
	private String tipoProducto;
	private String numeroPropuesta;
	private String fechSolicitud;
	private boolean checkSoles;
	private boolean checkDolares;
	private boolean checkSubPlanEducacion;
	private boolean checkSubPlanSuenio;
	private boolean checkSubPlanJubilacion;
	private boolean checkSubPlanVidaFree;
	private boolean checkSubPlanVidaFreePlus;
	private boolean checkSubPlanVidaFreeTotal;
	private SolicitudPDFPersonaBean asegurado;
	private SolicitudPDFPersonaBean contratante;
	private String vinculoAsegurado;
	private SolicitudPDFProductoBean producto;
	private String aseguradoIgualContratante;
	private List<SolicitudPDFBeneficiarioBean> beneficiarios;
	private SolicitudPDFDPSBean dps;
	
	private String agenteNombres;
	private String agenteCodigo;
	private String agenteAgencia;
	private boolean checkTratamientoAseguradoSi;
	private boolean checkTratamientoAseguradoNo;
	private boolean checkTratamientoContratanteSi;
	private boolean checkTratamientoContratanteNo;
	
	private String firmaContratante;
	private String firmaAsegurado;
	

}

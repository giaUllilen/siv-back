package pe.interseguro.siv.common.dto.response;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.interseguro.siv.common.dto.request.SolicitudFormularioContratanteResponseDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)

public class SolicitudFormularioResponseDTO extends BaseResponseDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7950159510259666070L;
	
	private Long idSolicitud;
	private String numeroCotizacion;
	private String moneda;
	private String monedaSimbolo;
	private SolicitudFormularioAseguradoResponseDTO asegurado;
	private SolicitudFormularioCotizacionResponseDTO cotizacion;
	private String aseguradoIgualContratante;
	private String vinculoAsegurado;
	private SolicitudFormularioContratanteResponseDTO contratante;
	private List<SolicitudFormularioBeneficiarioResponseDTO> beneficiarios;
	private SolicitudFormularioDPSResponseDTO dps;
    private String consentimientoAsegurado;
    private String consentimientoContratante;
    private String fechaFirmaAsegurado;
    private String fechaFirmaContratante;
    private String estadoSolicitud;
    private String subPlanCotizacionCodigo;
    private String flagExclusionCobAcc;
	

}

package pe.interseguro.siv.common.dto.request;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)

public class SolicitudGuardarRequestDTO extends BaseRequestDTO {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 3918735057356983220L;
	
	private Long idSolicitud;
    private String numeroCotizacion;
    private String usuarioLogin;
    private String correoUsuario;
    private SolicitudGuardarAseguradoRequestDTO asegurado;
    private SolicitudGuardarContratanteRequestDTO contratante;
    private String aseguradoIgualContratante;
    private String vinculoAsegurado;
    @Valid
    private List<SolicitudGuardarBeneficiarioRequestDTO> beneficiarios;
    private SolicitudGuardarDPSRequestDTO dps;
    private String consentimientoAsegurado;
    private String consentimientoContratante;
    private String flagExclusionCobAcc;
    private String idUsuario;

}

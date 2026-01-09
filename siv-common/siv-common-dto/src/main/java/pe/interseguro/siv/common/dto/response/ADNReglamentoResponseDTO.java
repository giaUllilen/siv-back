package pe.interseguro.siv.common.dto.response;

import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ADNReglamentoResponseDTO extends BaseResponseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9101799042850615978L;
	
	private ADNReglamentoTitularResponseDTO titular;
	private List<ADNReglamentoFamiliarResponseDTO> familiar;
	private List<ADNReglamentoReferidoResponseDTO> referido;
	private ADNReglamentoPlanFuturoResponsetDTO planFuturo;
	private String idAutoguardado;
	private String idCircuitoFirma;
	private Integer autoguardadoTiempo;
	private Integer idAdn;
	private Integer existeAdn;
	private Integer ldpdpActivo;
	private String existePlaft;
	private String codigoPlaft;
	private Map<String, Object> dataConsentimiento;
	
	
	public ADNReglamentoTitularResponseDTO getTitular() {
		return titular;
	}
	public void setTitular(ADNReglamentoTitularResponseDTO titular) {
		this.titular = titular;
	}
	public List<ADNReglamentoFamiliarResponseDTO> getFamiliar() {
		return familiar;
	}
	public void setFamiliar(List<ADNReglamentoFamiliarResponseDTO> familiar) {
		this.familiar = familiar;
	}
	public ADNReglamentoPlanFuturoResponsetDTO getPlanFuturo() {
		return planFuturo;
	}
	public void setPlanFuturo(ADNReglamentoPlanFuturoResponsetDTO planFuturo) {
		this.planFuturo = planFuturo;
	}
	public String getIdAutoguardado() {
		return idAutoguardado;
	}
	public void setIdAutoguardado(String idAutoguardado) {
		this.idAutoguardado = idAutoguardado;
	}
	public String getIdCircuitoFirma() {
		return idCircuitoFirma;
	}
	public void setIdCircuitoFirma(String idCircuitoFirma) {
		this.idCircuitoFirma = idCircuitoFirma;
	}
	public Integer getAutoguardadoTiempo() {
		return autoguardadoTiempo;
	}
	public void setAutoguardadoTiempo(Integer autoguardadoTiempo) {
		this.autoguardadoTiempo = autoguardadoTiempo;
	}
	public Integer getIdAdn() {
		return idAdn;
	}
	public void setIdAdn(Integer idAdn) {
		this.idAdn = idAdn;
	}
	public Integer getExisteAdn() {
		return existeAdn;
	}
	public void setExisteAdn(Integer existeAdn) {
		this.existeAdn = existeAdn;
	}
	
	
}

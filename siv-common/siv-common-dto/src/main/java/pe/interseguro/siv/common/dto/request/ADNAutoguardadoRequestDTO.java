package pe.interseguro.siv.common.dto.request;

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
public class ADNAutoguardadoRequestDTO extends BaseRequestDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2655785143553306924L;

	private ADNAutoguardadoTitularRequestDTO titular;
	private List<ADNAutoguardadoFamiliarRequestDTO> familiar;
	private ADNAutoguardadoPlanFuturoRequestDTO planFuturo;
	private String idAutoguardado;
	private String idCircuitoFirma;
	private Integer idAdn;
	private String idUsuario;
	public ADNAutoguardadoTitularRequestDTO getTitular() {
		return titular;
	}
	public void setTitular(ADNAutoguardadoTitularRequestDTO titular) {
		this.titular = titular;
	}
	public List<ADNAutoguardadoFamiliarRequestDTO> getFamiliar() {
		return familiar;
	}
	public void setFamiliar(List<ADNAutoguardadoFamiliarRequestDTO> familiar) {
		this.familiar = familiar;
	}
	public ADNAutoguardadoPlanFuturoRequestDTO getPlanFuturo() {
		return planFuturo;
	}
	public void setPlanFuturo(ADNAutoguardadoPlanFuturoRequestDTO planFuturo) {
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
	public Integer getIdAdn() {
		return idAdn;
	}
	public void setIdAdn(Integer idAdn) {
		this.idAdn = idAdn;
	}
	public String getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}
	
}

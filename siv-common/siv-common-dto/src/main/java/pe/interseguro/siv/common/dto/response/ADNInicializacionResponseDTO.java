package pe.interseguro.siv.common.dto.response;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor; 
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.interseguro.siv.common.bean.GenericoComboBean;
import pe.interseguro.siv.common.exception.ErrorResourceDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ADNInicializacionResponseDTO extends BaseResponseDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8055031618661748226L;
	
	private List<GenericoComboBean> tipoDocumento;
	private List<GenericoComboBean> genero;
	private List<GenericoComboBean> profesion;
	private List<GenericoComboBean> actividadEconomica;
	private List<GenericoComboBean> lugarTrabajo;
	private List<GenericoComboBean> fumador;
	private List<GenericoComboBean> tipoRelacion;

	private List<GenericoComboBean> estadoCivil;
	private List<GenericoComboBean> nacionalidad;
	private List<GenericoComboBean> tipoDireccion;
	private List<GenericoComboBean> tipoRelacionSolicitud;
	private List<GenericoComboBean> departamento;
	private List<GenericoComboBean> provincia;
	private List<GenericoComboBean> distrito;
	private List<GenericoComboBean> operadorFinanciero;
	private List<GenericoComboBean> entidadBancaria;
	private List<GenericoComboBean> tipoCuenta;
	private List<GenericoComboBean> moneda;
	
	private ADNInicializacionParametroResponseDTO parametro;
	private List<ADNInicializacionViaCobroResponseDTO> viasCobro;
	
	public List<GenericoComboBean> getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(List<GenericoComboBean> tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public List<GenericoComboBean> getGenero() {
		return genero;
	}
	public void setGenero(List<GenericoComboBean> genero) {
		this.genero = genero;
	}
	public List<GenericoComboBean> getProfesion() {
		return profesion;
	}
	public void setProfesion(List<GenericoComboBean> profesion) {
		this.profesion = profesion;
	}
	public List<GenericoComboBean> getActividadEconomica() {
		return actividadEconomica;
	}
	public void setActividadEconomica(List<GenericoComboBean> actividadEconomica) {
		this.actividadEconomica = actividadEconomica;
	}
	public List<GenericoComboBean> getFumador() {
		return fumador;
	}
	public void setFumador(List<GenericoComboBean> fumador) {
		this.fumador = fumador;
	}
	public List<GenericoComboBean> getTipoRelacion() {
		return tipoRelacion;
	}
	public void setTipoRelacion(List<GenericoComboBean> tipoRelacion) {
		this.tipoRelacion = tipoRelacion;
	}
	public ADNInicializacionParametroResponseDTO getParametro() {
		return parametro;
	}
	public void setParametro(ADNInicializacionParametroResponseDTO parametro) {
		this.parametro = parametro;
	}
	
}

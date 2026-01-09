package pe.interseguro.siv.common.dto.response;

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
public class ADNReglamentoPlanFuturoBaseResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3851447935480847631L;

	private String planEducacion;
	private String planProyecto;
	private String planJubilacion;
	private String ingresoTitular;
	private String porcentajeIngreso;
	private String totalIngreso;
	private String anioProteccion;
	private String informacionAdicional;
	
	public String getPlanEducacion() {
		return planEducacion;
	}
	public void setPlanEducacion(String planEducacion) {
		this.planEducacion = planEducacion;
	}
	public String getPlanProyecto() {
		return planProyecto;
	}
	public void setPlanProyecto(String planProyecto) {
		this.planProyecto = planProyecto;
	}
	public String getPlanJubilacion() {
		return planJubilacion;
	}
	public void setPlanJubilacion(String planJubilacion) {
		this.planJubilacion = planJubilacion;
	}
	public String getIngresoTitular() {
		return ingresoTitular;
	}
	public void setIngresoTitular(String ingresoTitular) {
		this.ingresoTitular = ingresoTitular;
	}
	public String getPorcentajeIngreso() {
		return porcentajeIngreso;
	}
	public void setPorcentajeIngreso(String porcentajeIngreso) {
		this.porcentajeIngreso = porcentajeIngreso;
	}
	public String getTotalIngreso() {
		return totalIngreso;
	}
	public void setTotalIngreso(String totalIngreso) {
		this.totalIngreso = totalIngreso;
	}
	public String getAnioProteccion() {
		return anioProteccion;
	}
	public void setAnioProteccion(String anioProteccion) {
		this.anioProteccion = anioProteccion;
	}	
	
	
}

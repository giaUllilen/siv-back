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
public class ADNInicializacionParametroResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5425945714817888878L;
	
	private Double porcentajeIngresoIni;
	private Double porcentajeIngresoFin;
	private Double seguroVidaLeyRegla;
	private Integer porcentajeIngresoDefault;
	private String pdfEvidencias;
	private String pdfEducacion;
	private String pdfProyectos;
	private String pdfJubilacion;
	private String itemParentezco;
	private String itemVinculoAsegurado;
	private String itemVinculoAseguradoRuc;
	
	public ADNInicializacionParametroResponseDTO(Double porcentajeIngresoIni, Double porcentajeIngresoFin, Double seguroVidaLeyRegla) {
		this.porcentajeIngresoIni = porcentajeIngresoIni;
		this.porcentajeIngresoFin = porcentajeIngresoFin;
		this.seguroVidaLeyRegla = seguroVidaLeyRegla;
	}

	public Double getPorcentajeIngresoIni() {
		return porcentajeIngresoIni;
	}
	public void setPorcentajeIngresoIni(Double porcentajeIngresoIni) {
		this.porcentajeIngresoIni = porcentajeIngresoIni;
	}
	public Double getPorcentajeIngresoFin() {
		return porcentajeIngresoFin;
	}
	public void setPorcentajeIngresoFin(Double porcentajeIngresoFin) {
		this.porcentajeIngresoFin = porcentajeIngresoFin;
	}
	public Double getSeguroVidaLeyRegla() {
		return seguroVidaLeyRegla;
	}
	public void setSeguroVidaLeyRegla(Double seguroVidaLeyRegla) {
		this.seguroVidaLeyRegla = seguroVidaLeyRegla;
	}

	public Integer getPorcentajeIngresoDefault() {
		return porcentajeIngresoDefault;
	}

	public void setPorcentajeIngresoDefault(Integer porcentajeIngresoDefault) {
		this.porcentajeIngresoDefault = porcentajeIngresoDefault;
	}

	public String getPdfEvidencias() {
		return pdfEvidencias;
	}

	public void setPdfEvidencias(String pdfEvidencias) {
		this.pdfEvidencias = pdfEvidencias;
	}

	public String getPdfEducacion() {
		return pdfEducacion;
	}

	public void setPdfEducacion(String pdfEducacion) {
		this.pdfEducacion = pdfEducacion;
	}

	public String getPdfProyectos() {
		return pdfProyectos;
	}

	public void setPdfProyectos(String pdfProyectos) {
		this.pdfProyectos = pdfProyectos;
	}

	public String getPdfJubilacion() {
		return pdfJubilacion;
	}

	public void setPdfJubilacion(String pdfJubilacion) {
		this.pdfJubilacion = pdfJubilacion;
	}
	
	
}

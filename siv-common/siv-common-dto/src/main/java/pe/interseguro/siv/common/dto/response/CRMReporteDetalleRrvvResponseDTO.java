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
public class CRMReporteDetalleRrvvResponseDTO implements Serializable {
	
	private String cuspp;
	private String documentoProspecto;
	private String prospecto;
	private String oportunidad;
	private String categoria;
	private String modulo;
	private String estado;
	private String saldoCICTotal;
	private String fechaNacimientoProspecto;
	private String fechaAsignacionProspecto;
	private String ramProspecto;
	private String fechaUltimoAporte;
	private String pronostico;
	private String direccionParticularProspecto;
	private String distritoParticularProspecto;
	private String provinciaParticularProspecto;
	private String departamentoParticularProspecto;
	private String scoring;
	private String fechaUltimaCitaCompletada;
	private String propietario;
	private String administradorUsuarioPropietario;
	private String divisa;
	private String ultimaActividadCompletada;
	private String fechaUltimaActividadCompletada;
	private String periodoPlanTrabajo;
	private String cumplimientoPlanTrabajo;
}

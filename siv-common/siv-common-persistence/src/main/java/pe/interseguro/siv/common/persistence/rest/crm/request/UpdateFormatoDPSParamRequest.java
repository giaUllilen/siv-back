package pe.interseguro.siv.common.persistence.rest.crm.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateFormatoDPSParamRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3921727851189485221L;

	@JsonProperty("CotizacionId")
	private String cotizacionId;
	
	@JsonProperty("inter_estatura_cm")
	private String estaturaCm;

	@JsonProperty("inter_peso_kg")
	private String pesoKg;

	@JsonProperty("inter_variacion_mas_de_5kg")
	private String variacionMasDe5kg;

	@JsonProperty("inter_usted_fuma_cigarros")
	private String ustedFumaCigarros;

	@JsonProperty("inter_usted_consume_alcohol")
	private String ustedConsumeAlcohol;

	@JsonProperty("inter_toma_medicamentos")
	private String tomaMedicamentos;

	@JsonProperty("inter_evaluacion_diagnostico")
	private String evaluacionDiagnostico;

	@JsonProperty("inter_otra_informacion_concerniente")
	private String otraInformacionConcerniente;

	@JsonProperty("inter_usted_consume_drogas")
	private String ustedConsumeDrogas;

	@JsonProperty("inter_tratamiento_quirurgico")
	private String tratamientoQuirurgico;

	@JsonProperty("inter_problema_fisico_mental")
	private String problemaFisicoMental;

	@JsonProperty("inter_embarazada")
	private String embarazada;

	@JsonProperty("inter_alto_riesgo")
	private String altoRiesgo;

	@JsonProperty("inter_actividad_profesional_laboral")
	private String actividadProfesionalLaboral;

	@JsonProperty("inter_practica_deportes_riesgo")
	private String practicaDeportesRiesgo;

	@JsonProperty("inter_participa_entrenamientos_competencias")
	private String participaEntrenamientosCompetencias;

	/*INI-GTI.23707: EN CRM ESTA AL REVES, POR ESO SE CAMBIA*/
	@JsonProperty("inter_participa_en_competencias")
	private String otrosDeportesHobbies;

	@JsonProperty("inter_otros_deportes_hobbies")
	private String participaEnCompetencias;
	/*FIN-GTI.23707*/
	@JsonProperty("inter_utiliza_moto")
	private String utilizaMoto;

	@JsonProperty("inter_asegurado_titular")
	private String aseguradoTitular;

	@JsonProperty("inter_conyuge")
	private String conyuge;

	@JsonProperty("inter_viaja_anio_aseg_titular")
	private String viajaAnioAsegTitular;

	@JsonProperty("inter_viaja_anio_conyuge")
	private String viajaAnioConyuge;

	@JsonProperty("inter_extralaborales_aseg_titular")
	private String extraLaboralesAsegTitular;

	@JsonProperty("inter_extralaborales_conyuge")
	private String extraLaboralesConyuge;

	@JsonProperty("inter_monitoreado_medico_aseg_titular")
	private String monitoreadoDedicoAsegTitular;

	@JsonProperty("inter_monitoreado_medico_conyuge")
	private String monitoreadoMedicoConyuge;

	@JsonProperty("inter_grado_discapacidad_aseg_titular")
	private String gradoDiscapacidadAsegTitular;

	@JsonProperty("inter_grado_discapacidad_conyuge")
	private String gradoDiscapacidadConyuge;

	@JsonProperty("inter_alcoholismo_drogadiccion_aseg_titular")
	private String alcoholismoDrogadiccionAsegTitular;

	@JsonProperty("inter_alcoholismo_drogadiccion_conyuge")
	private String alcoholismoDrogadiccionConyuge;

	@JsonProperty("inter_practica_deporte_aficionado_aseg_titular")
	private String practicaDeporteAficionadoAsegTitular;

	@JsonProperty("inter_practica_deporte_aficionado_conyuge")
	private String practicaDeporteAficionadoConyuge;

	@JsonProperty("inter_aficionado_remunerado_aseg_titular")
	private String aficionadoRemuneradoAsegTitular;

	@JsonProperty("inter_aficionado_remunerado_conyuge")
	private String aficionadoRemuneradoConyuge;

	@JsonProperty("inter_profesional_aseg_titular")
	private String profesionalAsegTitular;

	@JsonProperty("inter_profesional_conyuge")
	private String profesionalConyuge;

	@JsonProperty("inter_participante_aseg_titular")
	private String participanteAsegTitular;

	@JsonProperty("inter_participante_conyuge")
	private String participanteConyuge;

	@JsonProperty("inter_medio_transporte_aseg_titular")
	private String medioTransporteAsegTitular;

	@JsonProperty("inter_medio_transporte_conyuge")
	private String medioTransporteConyuge;

	@JsonProperty("inter_distraccion_aseg_titular")
	private String distraccionAsegTitular;

	@JsonProperty("inter_distraccion_conyuge")
	private String distraccionConyuge;

	@JsonProperty("inter_deporte_entrenamiento_aseg_titular")
	private String deporteEntrenamientoAsegTitular;

	@JsonProperty("inter_deporte_entrenamiento_conyuge")
	private String deporteEntrenamientoConyuge;


}

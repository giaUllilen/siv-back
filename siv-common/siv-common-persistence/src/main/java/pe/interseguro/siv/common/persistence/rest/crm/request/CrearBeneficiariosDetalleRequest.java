package pe.interseguro.siv.common.persistence.rest.crm.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearBeneficiariosDetalleRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2446758754069610047L;

	@JsonProperty("inter_nombres")
	private String nombres;

	@JsonProperty("inter_apellidopaterno")
	private String apellidoPaterno;

	@JsonProperty("inter_apellidomaterno")
	private String apellidoMaterno;

	@JsonProperty("inter_numerodocumento")
	private String numeroDocumento;
	
	@JsonProperty("inter_tipodedocumento")
	private String tipoDocumento;

	@JsonProperty("inter_fechadenacimiento")
	private String fechaNacimiento;

	@JsonProperty("inter_tipoderelacion")
	private String tipoRelacion;

	@JsonProperty("inter_porcentajedistribucion")
	private String porcentajeDistribucion;

	@JsonProperty("inter_beneficiariocontigente")
	private String beneficiarioContigente;

	@JsonProperty("inter_beneficiarionormal")
	private String beneficiarioNormal;
}

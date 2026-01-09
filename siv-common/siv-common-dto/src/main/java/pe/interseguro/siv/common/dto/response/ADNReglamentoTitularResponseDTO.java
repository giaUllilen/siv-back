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
public class ADNReglamentoTitularResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4841830464998584051L;
	
	private String tipoDocumento;
	private String numeroDocumento;
	private String fechaNacimiento;
	private String nombres;
	private String apellidoPaterno;
	private String apellidoMaterno;
	private String razonSocial;
	private String genero;
	private String celular;
	private String correo;
	private String profesion;
	private String profesionDetalle;
	private String actividadEconomica;
	private String fumador;
	private Integer edadActuarial;
	private String estadoCivil;
	private String ingresoMoneda;
	private String ingresoMonto;
	private String tipoCasa;
	private String tieneVehiculo;
	private String tieneAfp;
	private String tieneSeguro;
	private String nacionalidad;
	private String centroTrabajo;
	
	private ADNReglamentoTitularDireccionResponseDTO direccion;
}

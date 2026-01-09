package pe.interseguro.siv.common.dto.request;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Range;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ADNRegistroTitularRequestDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3408004822231217270L;

	@NotNull
	@Range(min = 1L, max = 2L)
	private Integer tipoDocumento;

	@NotBlank
	private String numeroDocumento;

	@NotBlank
	@Pattern(regexp = "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)")
	private String fechaNacimiento;

	private Integer edadActuarial;
	
	@NotBlank
	private String nombres;

	@NotBlank
	private String apellidoPaterno;
	
	@NotBlank
	private String apellidoMaterno;
	
	@NotNull
	@Range(min = 1L, max = 2L)
	private Integer genero;

	@NotNull
	private Integer celular;

	@NotBlank
	@Email
	private String correo;
	
	@NotNull
	@Range(min = 1L, max = 112L)
	private Integer profesion;

	@NotNull
	@Range(min = 1L, max = 293L)
	private Integer actividadEconomica;
	
	@NotNull
	@Range(min = 1L, max = 2L)
	private Integer fumador;

	private String tipoCasa;
	
	@Range(min = 1L, max = 2L)
	private Integer tieneVehiculo;
	
	@Range(min = 1L, max = 2L)
	private Integer tieneAfp;
	
	@Range(min = 1L, max = 2L)
	private Integer tieneSeguroVida;
}

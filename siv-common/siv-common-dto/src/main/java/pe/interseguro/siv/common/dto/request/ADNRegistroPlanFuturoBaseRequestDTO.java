package pe.interseguro.siv.common.dto.request;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

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
public class ADNRegistroPlanFuturoBaseRequestDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7244557595431259535L;

	@NotNull
	@Range(min = 0L, max = 6L)
	private Integer planEducacion;
	
	@NotNull
	@Range(min = 0L, max = 6L)
	private Integer planProyecto;

	@NotNull
	@Range(min = 0L, max = 6L)
	private Integer planJubilacion;
	
	@NotNull
	private Double ingresoTitular;

	@NotNull
	private Double porcentajeIngreso;

	@NotNull
	private Double totalIngreso;

	@NotNull
	private Integer anioProteccion;
	
	private String informacionAdicional;

}

package pe.interseguro.siv.common.dto.request;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
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
public class ADNRegistroRequestDTO extends BaseRequestDTO implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 5136550784368979708L;

	
	@NotNull
	@Range(min = 1L, max = 2L)
	private Integer tipo;
	
	@Valid
	private ADNRegistroTitularRequestDTO titular;
	
	private List<ADNRegistroFamiliarRequestDTO> familiar;
	
	private ADNRegistroPlanFuturoRequestDTO planFuturo;

	private String idAutoguardado;
	
	//@NotBlank
	private String idCircuitoFirma;
	
	private String idCircuitoFirmaAntigua;
	
	@NotBlank
	private String idUsuario;
	
	private Long idAdn;
	
	@Range(min = 1L, max = 2L)
	private Integer indicadorCambio;

	@Range(min = 1L, max = 2L)
	private Integer formulario;

	private String idUsuarioCRM;
	
	private String codigoVendedorCRM;

	private String nombreVendedorCRM;
	
	private String flagLDPDP;
	
	private String fechaLDPDP;
	
	private String correoUsuario;
	
	private String nombreAgente;
}
